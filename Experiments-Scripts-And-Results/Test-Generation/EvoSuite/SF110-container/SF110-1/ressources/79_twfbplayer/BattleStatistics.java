package de.outstare.fortbattleplayer.statistics;

import java.util.concurrent.atomic.*;
import java.util.logging.*;
import de.outstare.fortbattleplayer.model.*;
import java.util.*;

public class BattleStatistics implements DynamicStatistics, CombatantObserver
{
    private static final transient Logger LOG;
    private final Map<Combatant, OnlineCounter> onliners;
    private final Map<CombatantSide, List<CriticalHit>> critHits;
    private final Map<CombatantSide, Set<PositionSwitch>> swapList;
    private final AllCombatantSidesCounter crits;
    private final AllCombatantSidesCounter moves;
    private final AllCombatantSidesCounter swaps;
    private final AllCombatantSidesCounter attackBonuses;
    private final AllCombatantSidesCounter defenseBonuses;
    private final Map<Combatant, Counter> shots;
    private final Map<CombatantSide, RoundStatistics> shotsPerRound;
    private final Map<CombatantSide, RoundStatistics> hitsPerRound;
    private final Map<CombatantSide, AtomicInteger> shotRoundCounter;
    private final Map<CombatantSide, AtomicInteger> hitRoundCounter;
    private final Map<Combatant, Counter> victims;
    private final List<Combatant> turnOrder;
    private final List<AllCombatantSidesCounter> actorsPerRound;
    private final Map<Area, AreaStatistic> fieldStats;
    private final int _numberOfDefenders;
    private final int _numberOfAttacker;
    private int currentRoundNo;
    private Boolean firstRound;
    private AllCombatantSidesCounter actorsThisRound;
    
    public void setRound(final int no) {
        if (this.firstRound == null) {
            this.firstRound = Boolean.TRUE;
        }
        else if (this.firstRound) {
            this.firstRound = Boolean.FALSE;
            for (final Combatant player : this.onliners.keySet()) {
                if (!this.turnOrder.contains(player)) {
                    BattleStatistics.LOG.info("could not determine turn order for " + player);
                }
            }
        }
        this.addAndResetRoundCounter(this.shotRoundCounter, this.shotsPerRound);
        this.addAndResetRoundCounter(this.hitRoundCounter, this.hitsPerRound);
        this.actorsPerRound.add(this.actorsThisRound);
        this.actorsThisRound = new AllCombatantSidesCounter();
        this.currentRoundNo = no;
    }
    
    void addAndResetRoundCounter(final Map<CombatantSide, AtomicInteger> roundCounter, final Map<CombatantSide, RoundStatistics> allRounds) {
        for (final Map.Entry<CombatantSide, AtomicInteger> mapping : roundCounter.entrySet()) {
            final CombatantSide side = mapping.getKey();
            final AtomicInteger roundShots = mapping.getValue();
            allRounds.get(side).addValue(roundShots.get());
            roundShots.set(0);
        }
    }
    
    int combatantLifetime(final Combatant combatant) {
        final OnlineCounter onAndOffline = this.onliners.get(combatant);
        if (onAndOffline == null) {
            return 0;
        }
        return onAndOffline.sumOfEvents();
    }
    
    public BattleStatistics(final int numberOfAttacker, final int numberOfDefenders) {
        this.onliners = new HashMap<Combatant, OnlineCounter>();
        this.critHits = new HashMap<CombatantSide, List<CriticalHit>>();
        for (final CombatantSide side : CombatantSide.values()) {
            this.critHits.put(side, new ArrayList<CriticalHit>());
        }
        this.swapList = new HashMap<CombatantSide, Set<PositionSwitch>>();
        for (final CombatantSide side : CombatantSide.values()) {
            this.swapList.put(side, new HashSet<PositionSwitch>());
        }
        this.crits = new AllCombatantSidesCounter();
        this.moves = new AllCombatantSidesCounter();
        this.swaps = new AllCombatantSidesCounter();
        this.attackBonuses = new AllCombatantSidesCounter();
        this.defenseBonuses = new AllCombatantSidesCounter();
        this.shots = new HashMap<Combatant, Counter>();
        this.shotsPerRound = new HashMap<CombatantSide, RoundStatistics>();
        for (final CombatantSide side : CombatantSide.values()) {
            this.shotsPerRound.put(side, new RoundStatistics("shots", "shots"));
        }
        this.hitsPerRound = new HashMap<CombatantSide, RoundStatistics>();
        for (final CombatantSide side : CombatantSide.values()) {
            this.hitsPerRound.put(side, new RoundStatistics("hits", "hits"));
        }
        this.shotRoundCounter = new HashMap<CombatantSide, AtomicInteger>();
        for (final CombatantSide side : CombatantSide.values()) {
            this.shotRoundCounter.put(side, new AtomicInteger());
        }
        this.hitRoundCounter = new HashMap<CombatantSide, AtomicInteger>();
        for (final CombatantSide side : CombatantSide.values()) {
            this.hitRoundCounter.put(side, new AtomicInteger());
        }
        this.victims = new HashMap<Combatant, Counter>();
        this.turnOrder = new LinkedList<Combatant>();
        this.actorsPerRound = new ArrayList<AllCombatantSidesCounter>(50);
        this.fieldStats = new HashMap<Area, AreaStatistic>();
        this.currentRoundNo = 0;
        this.firstRound = null;
        this.actorsThisRound = new AllCombatantSidesCounter();
        this._numberOfAttacker = numberOfAttacker;
        this._numberOfDefenders = numberOfDefenders;
    }
    
    int round(final double number) {
        return (int)Math.round(number);
    }
    
    @Override
    public int percentAbleToShoot(final CombatantSide side) {
        double sum = 0.0;
        int players = 0;
        for (final Map.Entry<Combatant, Counter> playerShots : this.shots.entrySet()) {
            final Combatant combatant = playerShots.getKey();
            if (this.isOnSide(side, combatant)) {
                final int lifetime = this.combatantLifetime(combatant);
                final int roundsShooting = playerShots.getValue().getValue();
                sum += roundsShooting * 100.0 / lifetime;
                ++players;
            }
        }
        if (players == 0) {
            return 0;
        }
        return this.round(sum / players);
    }
    
    @Override
    public int amountOfAttackBonus(final CombatantSide side) {
        return this.attackBonuses.getSideValue(side);
    }
    
    @Override
    public int amountOfDefenseBonus(final CombatantSide side) {
        return this.defenseBonuses.getSideValue(side);
    }
    
    @Override
    public double numberOfMovesPerPlayer(final CombatantSide side) {
        double result = 0.0;
        switch (side) {
            case ATTACKER: {
                result = this.moves.getSideValue(side) / this._numberOfAttacker;
                break;
            }
            case DEFENDER: {
                result = this.moves.getSideValue(side) / this._numberOfDefenders;
                break;
            }
            default: {
                result = this.moves.getSideValue(null) / (this._numberOfAttacker + this._numberOfDefenders);
                break;
            }
        }
        return result;
    }
    
    @Override
    public int numberOfOffliners(final CombatantSide side) {
        int offliner = 0;
        for (final Map.Entry<Combatant, OnlineCounter> playerStats : this.onliners.entrySet()) {
            if (this.isOnSide(side, playerStats.getKey())) {
                final OnlineCounter counter = playerStats.getValue();
                if (!counter.wasOffline()) {
                    continue;
                }
                ++offliner;
            }
        }
        return offliner;
    }
    
    private boolean isOnSide(final CombatantSide side, final Combatant combatant) {
        return side == null || side.equals(combatant.getSide());
    }
    
    @Override
    public double roundsTillOnline(final CombatantSide side) {
        int combatants = 0;
        int rounds = 0;
        for (final Map.Entry<Combatant, OnlineCounter> playerStats : this.onliners.entrySet()) {
            if (this.isOnSide(side, playerStats.getKey())) {
                final OnlineCounter counter = playerStats.getValue();
                if (counter.wasOffline()) {
                    continue;
                }
                ++combatants;
                rounds += counter.firstOnline();
            }
        }
        if (combatants == 0) {
            return 0.0;
        }
        return rounds / combatants;
    }
    
    @Override
    public double averageRoundsOnline(final CombatantSide side) {
        double combatants = 0.0;
        double rounds = 0.0;
        for (final Map.Entry<Combatant, OnlineCounter> playerStats : this.onliners.entrySet()) {
            if (this.isOnSide(side, playerStats.getKey())) {
                final OnlineCounter counter = playerStats.getValue();
                if (counter.wasOffline()) {
                    continue;
                }
                ++combatants;
                rounds += counter.onlineEvents();
            }
        }
        if (combatants == 0.0) {
            return 0.0;
        }
        return rounds / combatants;
    }
    
    @Override
    public void hasMoved(final Combatant combatant, final Area newPos) {
        this.moves.incrementSide(combatant);
    }
    
    @Override
    public void newDestination(final Combatant combatant, final Area destination) {
    }
    
    @Override
    public void aimsAt(final Combatant combatant, final Combatant target) {
        this.incrementCombatantCounter(this.shots, combatant);
        this.incrementCombatantCounter(this.victims, target);
        this.shotRoundCounter.get(combatant.getSide()).incrementAndGet();
        final Area field = target._getLocation();
        this.getFieldStat(field).addShot();
    }
    
    private void incrementCombatantCounter(final Map<Combatant, Counter> combatantCounters, final Combatant combatant) {
        if (!combatantCounters.containsKey(combatant)) {
            combatantCounters.put(combatant, new Counter());
        }
        combatantCounters.get(combatant).increment();
    }
    
    @Override
    public void isHit(final Combatant combatant, final int damage, final int oldHealthAmount) {
        this.hitRoundCounter.get(combatant.getSide()).incrementAndGet();
        final Area field = combatant._getLocation();
        this.getFieldStat(field).addHit();
    }
    
    @Override
    public void isDead(final Combatant combatant) {
    }
    
    @Override
    public void isAlive(final Combatant combatant) {
    }
    
    @Override
    public void isOnline(final Combatant combatant, final boolean changed) {
        if (!this.onliners.containsKey(combatant)) {
            this.onliners.put(combatant, new OnlineCounter());
        }
        if (combatant.isOnline()) {
            this.onliners.get(combatant).addOnline();
        }
        else {
            this.onliners.get(combatant).addOffline();
        }
        this.combatantTurn(combatant);
    }
    
    private void combatantTurn(final Combatant combatant) {
        final SectorBonus bonus = combatant.getSectorBonus();
        this.attackBonuses.incrementBy(bonus.attackBonus, combatant);
        this.defenseBonuses.incrementBy(bonus.defendBonus, combatant);
        if (this.firstRound != null && this.firstRound) {
            this.turnOrder.add(combatant);
        }
        if (BattleStatistics.LOG.isLoggable(Level.FINE) && (this.firstRound == null || !this.firstRound) && !this.turnOrder.contains(combatant) && this.currentRoundNo > 1) {
            BattleStatistics.LOG.fine("---- not in order: " + combatant);
        }
        this.actorsThisRound.incrementSide(combatant);
    }
    
    @Override
    public String targetNoOne(final CombatantSide side) {
        String result = "";
        int max = 0;
        for (final Map.Entry<Combatant, Counter> victim : this.victims.entrySet()) {
            final Combatant combatant = victim.getKey();
            if (this.isOnSide(side, combatant)) {
                final int receivedShots = victim.getValue().getValue();
                if (receivedShots <= max) {
                    continue;
                }
                max = receivedShots;
                result = combatant.getName() + "(" + receivedShots + ")";
            }
        }
        return result;
    }
    
    @Override
    public int numberOfBayonets(final CombatantSide side) {
        return this.countWeaponMods(side, new WeaponModCounter.BayonetCounter());
    }
    
    int countWeaponMods(final CombatantSide side, final WeaponModCounter counter) {
        final Set<Combatant> players = this.onliners.keySet();
        int bayonets = 0;
        for (final Combatant combatant : players) {
            if (this.isOnSide(side, combatant)) {
                final Weapon playerWeapon = combatant.getWeapon();
                if (!counter.counts(playerWeapon)) {
                    continue;
                }
                ++bayonets;
            }
        }
        return bayonets;
    }
    
    @Override
    public int numberOfGraphitLubricants(final CombatantSide side) {
        return this.countWeaponMods(side, new WeaponModCounter.GraphitLubricantCounter());
    }
    
    @Override
    public int numberOfFettesOil(final CombatantSide side) {
        return this.countWeaponMods(side, new WeaponModCounter.FettesOilCounter());
    }
    
    @Override
    public int numberOfSchmierOil(final CombatantSide side) {
        return this.countWeaponMods(side, new WeaponModCounter.SchmierOilCounter());
    }
    
    @Override
    public int numberOfShinyOil(final CombatantSide side) {
        return this.countWeaponMods(side, new WeaponModCounter.ShinyOilCounter());
    }
    
    @Override
    public int numberOfLoadingChamerOrEnhancedPatrons(final CombatantSide side) {
        return this.countWeaponMods(side, new WeaponModCounter.LoadingchamberOrEnhancedPatronsCounter());
    }
    
    @Override
    public int numberOfHipFlasks(final CombatantSide side) {
        return this.countWeaponMods(side, new WeaponModCounter.HipFlaskCounter());
    }
    
    @Override
    public void hasSwappedPosition(final Combatant combatant, final Combatant swappedWith) {
        this.swaps.incrementSide(combatant);
        final PositionSwitch swap = new PositionSwitch(this.currentRoundNo, combatant, swappedWith);
        assert this.swapList.get(combatant.getSide()) != null;
        this.swapList.get(combatant.getSide()).add(swap);
    }
    
    @Override
    public int totalSwaps(final CombatantSide side) {
        return this.swaps.getSideValue(side);
    }
    
    @Override
    public Set<PositionSwitch> switchedPosList(final CombatantSide side) {
        Set<PositionSwitch> allSwaps;
        if (side == null) {
            allSwaps = new HashSet<PositionSwitch>();
            for (final Set<PositionSwitch> sideSwitches : this.swapList.values()) {
                allSwaps.addAll(sideSwitches);
            }
        }
        else {
            allSwaps = this.swapList.get(side);
        }
        return allSwaps;
    }
    
    @Override
    public void criticalShot(final Combatant combatant, final Combatant victim, final int damage) {
        final CriticalHit crit = new CriticalHit(combatant, victim, damage, this.currentRoundNo);
        this.critHits.get(combatant.getSide()).add(crit);
        this.crits.incrementSide(combatant);
        final Area field = victim._getLocation();
        this.getFieldStat(field).addCrit();
    }
    
    @Override
    public List<CriticalHit> critList(final CombatantSide side) {
        List<CriticalHit> allCrits;
        if (side == null) {
            allCrits = new ArrayList<CriticalHit>();
            for (final List<CriticalHit> sideCrits : this.critHits.values()) {
                allCrits.addAll(sideCrits);
            }
        }
        else {
            allCrits = this.critHits.get(side);
        }
        return allCrits;
    }
    
    @Override
    public int criticalHits(final CombatantSide side) {
        return this.crits.getSideValue(side);
    }
    
    @Override
    public long criticalHitDamage(final CombatantSide side) {
        long sum = 0L;
        for (final CriticalHit crit : this.critList(side)) {
            sum += crit.critOnlyDamage;
        }
        return sum;
    }
    
    @Override
    public List<Combatant> turnOrder(final CombatantSide side) {
        final List<Combatant> sidePlayers = new ArrayList<Combatant>(this.turnOrder.size() / 2);
        for (final Combatant player : this.turnOrder) {
            if (side == null || player.getSide() == side) {
                sidePlayers.add(player);
            }
        }
        return sidePlayers;
    }
    
    @Override
    public Map<CombatantSide, ? extends LabeledData> getShotsPerRound() {
        return this.shotsPerRound;
    }
    
    @Override
    public Map<CombatantSide, ? extends LabeledData> getHitsPerRound() {
        return this.hitsPerRound;
    }
    
    @Override
    public Map<CombatantSide, ? extends LabeledData> getNotShootersPerRound() {
        final Map<CombatantSide, RoundStatistics> nonShootersPerRound = new HashMap<CombatantSide, RoundStatistics>();
        for (final CombatantSide side : CombatantSide.values()) {
            final RoundStatistics nonShooters = new RoundStatistics("Not shooting", "players");
            nonShootersPerRound.put(side, nonShooters);
            final LabeledData shotRoundCounts = this.shotsPerRound.get(side);
            if (shotRoundCounts != null) {
                final double[] shotCounts = shotRoundCounts.toArray();
                if (this.actorsPerRound.size() == shotCounts.length) {
                    for (int i = 0; i < shotCounts.length; ++i) {
                        final int actors = this.actorsPerRound.get(i).getSideValue(side);
                        final double numberNotShooting = actors - shotCounts[i];
                        nonShooters.addValue(numberNotShooting);
                    }
                }
                else {
                    BattleStatistics.LOG.warning("round statistics for players and shots differ!");
                }
            }
            else {
                BattleStatistics.LOG.warning("no data for players and shots for side " + side);
            }
        }
        return nonShootersPerRound;
    }
    
    private AreaStatistic getFieldStat(final Area area) {
        if (!this.fieldStats.containsKey(area)) {
            this.fieldStats.put(area, new AreaStatistic(area));
        }
        return this.fieldStats.get(area);
    }
    
    @Override
    public Map<Area, AreaStatistic> getFieldStatistics() {
        return this.fieldStats;
    }
    
    static {
        LOG = Logger.getLogger(BattleStatistics.class.getName());
    }
}
