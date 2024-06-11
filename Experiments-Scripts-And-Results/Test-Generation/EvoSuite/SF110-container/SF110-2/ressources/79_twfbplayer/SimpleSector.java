package de.outstare.fortbattleplayer.model.impl;

import java.util.*;
import de.outstare.fortbattleplayer.model.*;

public class SimpleSector implements Sector
{
    private final int height;
    private final boolean defenderSpawn;
    private final boolean attackerSpawn;
    private final int attackBonus;
    private final int defendBonus;
    private final boolean flag;
    private final int classBonus;
    private final CharacterClass classType;
    private CombatantSide occupier;
    private int occupierCount;
    private final Set<Area> areas;
    private final Set<SectorObserver> observers;
    
    public SimpleSector(final int height, final boolean defenderSpawn, final boolean attackerSpawn, final int attackerBonus, final int defenderBonus, final boolean flag, final int classBonus, final CharacterClass bonusClass) {
        this.occupier = null;
        this.occupierCount = 0;
        this.areas = new HashSet<Area>();
        this.observers = new HashSet<SectorObserver>();
        this.height = height;
        this.defenderSpawn = defenderSpawn;
        this.attackerSpawn = attackerSpawn;
        this.attackBonus = attackerBonus;
        this.defendBonus = defenderBonus;
        this.flag = flag;
        this.classBonus = classBonus;
        this.classType = bonusClass;
    }
    
    @Override
    public void free() {
        if (this.occupierCount > 0) {
            --this.occupierCount;
        }
        assert this.occupierCount >= 0 : "more combatants lefts this sector than enter it";
        if (this.occupierCount == 0) {
            this.occupier = null;
            this.occupierChanged();
        }
    }
    
    private void occupierChanged() {
        for (final SectorObserver observer : this.observers) {
            observer.occupierChanged();
        }
    }
    
    @Override
    public void gainControl(final CombatantSide side) {
        ++this.occupierCount;
        if (this.occupier != side) {
            this.occupier = side;
            this.occupierChanged();
        }
    }
    
    @Override
    public CombatantSide getOccupier() {
        return this.occupier;
    }
    
    @Override
    public boolean isOccupied() {
        return this.occupier != null;
    }
    
    @Override
    public int getHeight() {
        return this.height;
    }
    
    public boolean isDefenderSpawn() {
        return this.defenderSpawn;
    }
    
    public boolean isAttackerSpawn() {
        return this.attackerSpawn;
    }
    
    public boolean isFlag() {
        return this.flag;
    }
    
    @Override
    public SectorBonus getBonus(final CharacterClass charClass) {
        return new SectorBonus(this.getAttackBonus(charClass), this.getDefenseBonus(charClass), this.getDamageBonus());
    }
    
    protected int getDamageBonus() {
        return this.getBonusFromWeapons(SectorBonus.BonusType.DAMAGE);
    }
    
    private int getBonusFromWeapons(final SectorBonus.BonusType type) {
        int bonus = 0;
        for (final Area area : this.areas) {
            if (area.isOccupied()) {
                final Combatant combatant = area.getOccupier();
                final Weapon weapon = combatant.getWeapon();
                final int sectorBonus = weapon.getSectorBonus(type);
                if (sectorBonus != 0) {
                    bonus = sectorBonus;
                    break;
                }
                continue;
            }
        }
        return bonus;
    }
    
    protected int getAttackBonus(final CharacterClass charClass) {
        int bonus = this.attackBonus + this.classBonus(charClass);
        bonus += this.getBonusFromWeapons(SectorBonus.BonusType.ATTACK);
        return bonus;
    }
    
    protected int getDefenseBonus(final CharacterClass charClass) {
        int bonus = this.defendBonus + this.classBonus(charClass);
        bonus += this.getBonusFromWeapons(SectorBonus.BonusType.DEFENSE);
        return bonus;
    }
    
    protected int classBonus(final CharacterClass charClass) {
        if (this.classType != charClass) {
            return 0;
        }
        return this.classBonus;
    }
    
    @Override
    public Set<Area> getAreas() {
        return this.areas;
    }
    
    @Override
    public void _addArea(final Area area) {
        this.areas.add(area);
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = 31 * result + ((this.areas == null) ? 0 : this.areas.hashCode());
        result = 31 * result + this.attackBonus;
        result = 31 * result + (this.attackerSpawn ? 1231 : 1237);
        result = 31 * result + this.classBonus;
        result = 31 * result + ((this.classType == null) ? 0 : this.classType.hashCode());
        result = 31 * result + this.defendBonus;
        result = 31 * result + (this.defenderSpawn ? 1231 : 1237);
        result = 31 * result + (this.flag ? 1231 : 1237);
        result = 31 * result + this.height;
        return result;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof SimpleSector)) {
            return false;
        }
        final SimpleSector other = (SimpleSector)obj;
        if (this.areas == null) {
            if (other.areas != null) {
                return false;
            }
        }
        else if (!this.areas.equals(other.areas)) {
            return false;
        }
        return this.attackBonus == other.attackBonus && this.attackerSpawn == other.attackerSpawn && this.classBonus == other.classBonus && this.classType == other.classType && this.defendBonus == other.defendBonus && this.defenderSpawn == other.defenderSpawn && this.flag == other.flag && this.height == other.height;
    }
    
    @Override
    public void addObserver(final SectorObserver observer) {
        this.observers.add(observer);
    }
    
    @Override
    public void removeObserver(final SectorObserver observer) {
        this.observers.remove(observer);
    }
}
