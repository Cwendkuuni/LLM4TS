package net.virtualinfinity.atrobots.robot;

import net.virtualinfinity.atrobots.interrupts.*;
import net.virtualinfinity.atrobots.hardware.*;
import net.virtualinfinity.atrobots.hardware.heatsinks.*;
import net.virtualinfinity.atrobots.hardware.throttle.*;
import net.virtualinfinity.atrobots.hardware.turret.*;
import net.virtualinfinity.atrobots.hardware.transponder.*;
import net.virtualinfinity.atrobots.hardware.radio.*;
import net.virtualinfinity.atrobots.hardware.armor.*;
import net.virtualinfinity.atrobots.hardware.scanning.radar.*;
import net.virtualinfinity.atrobots.hardware.scanning.sonar.*;
import net.virtualinfinity.atrobots.hardware.mines.*;
import net.virtualinfinity.atrobots.hardware.shield.*;
import net.virtualinfinity.atrobots.ports.*;
import net.virtualinfinity.atrobots.hardware.scanning.*;
import net.virtualinfinity.atrobots.measures.*;
import net.virtualinfinity.atrobots.*;
import net.virtualinfinity.atrobots.arenaobjects.*;
import net.virtualinfinity.atrobots.snapshots.*;
import java.util.*;
import net.virtualinfinity.atrobots.arena.*;
import net.virtualinfinity.atrobots.computer.*;
import net.virtualinfinity.atrobots.hardware.missiles.*;

public class Robot extends TangibleArenaObject implements Resettable, HasHeading, Destructable, HasOverburner, MissileFactory, ArmorDepletionListener, ScanSource, DamageInflicter
{
    private final HeatSinks heatSinks;
    private final Odometer odometer;
    private final String name;
    private final int id;
    private final RobotScore score;
    private Throttle throttle;
    private Computer computer;
    private Turret turret;
    private Transponder transponder;
    private Transceiver transceiver;
    private Duration lastDamageGiven;
    private Duration lastDamageTaken;
    private Armor armor;
    private Radar radar;
    private Sonar sonar;
    private Temperature shutdownLevel;
    private MineLayer mineLayer;
    private Shield shield;
    private boolean overburn;
    private HardwareBus hardwareBus;
    private static final RelativeAngle STEERING_SPEED;
    private final Position oldPosition;
    private int roundKills;
    private final List<RobotListener> robotListeners;
    private final Heading desiredHeading;
    
    public Robot(final String name, final int id, final RobotScore score) {
        this.heatSinks = new HeatSinks();
        this.odometer = new Odometer();
        this.lastDamageGiven = Duration.fromCycles(0);
        this.lastDamageTaken = Duration.fromCycles(0);
        this.oldPosition = new Position();
        this.robotListeners = new ArrayList<RobotListener>();
        this.desiredHeading = new Heading(this.heading.getAngle());
        this.position.setOdometer(this.odometer);
        this.name = name;
        this.id = id;
        this.score = score;
        this.roundKills = 0;
    }
    
    public void setComputer(final Computer computer) {
        (this.computer = computer).setId(this.getId());
        computer.setName(this.getName());
    }
    
    public Computer getComputer() {
        return this.computer;
    }
    
    @Override
    public void destruct() {
        this.getArmor().destruct();
    }
    
    public Turret getTurret() {
        return this.turret;
    }
    
    @Override
    public void setOverburn(final boolean overburn) {
        this.overburn = overburn;
    }
    
    public Transponder getTransponder() {
        return this.transponder;
    }
    
    public Speed getSpeed() {
        return this.speed;
    }
    
    public Duration getLastDamageGiven() {
        return this.lastDamageGiven;
    }
    
    public Duration getLastDamageTaken() {
        return this.lastDamageTaken;
    }
    
    public Transceiver getTransceiver() {
        return this.transceiver;
    }
    
    public Odometer getOdometer() {
        return this.odometer;
    }
    
    public Throttle getThrottle() {
        return this.throttle;
    }
    
    public HeatSinks getHeatSinks() {
        return this.heatSinks;
    }
    
    @Override
    public Heading getHeading() {
        return this.heading;
    }
    
    public PortHandler getTurretOffsetSensor() {
        return new PortHandler() {
            @Override
            public short read() {
                return (short)Robot.this.getTurretShift();
            }
        };
    }
    
    public int getTurretShift() {
        return this.getTurret().getHeading().getAngle().getAngleCounterClockwiseTo(this.getHeading().getAngle()).getBygrees();
    }
    
    public Armor getArmor() {
        return this.armor;
    }
    
    public Radar getRadar() {
        return this.radar;
    }
    
    public PortHandler getAimTurretPort() {
        return new PortHandler() {
            @Override
            public void write(final short value) {
                Robot.this.setTurretOffset(RelativeAngle.fromBygrees(value));
            }
        };
    }
    
    private void setTurretOffset(final RelativeAngle angle) {
        this.getTurret().getHeading().setAngle(this.getHeading().getAngle().counterClockwise(angle));
    }
    
    public Heading getDesiredHeading() {
        return this.desiredHeading;
    }
    
    public Sonar getSonar() {
        return this.sonar;
    }
    
    public PortHandler getOverburnLatchPort() {
        return new PortHandler() {
            @Override
            public short read() {
                return (short)(Robot.this.isOverburn() ? 1 : 0);
            }
            
            @Override
            public void write(final short value) {
                Robot.this.setOverburn(value != 0);
            }
        };
    }
    
    @Override
    public boolean isOverburn() {
        return this.overburn;
    }
    
    public PortHandler getShutdownLevelLatchPort() {
        return new PortHandler() {
            @Override
            public short read() {
                return (short)Robot.this.hardwareBus.getShutdownLevel();
            }
            
            @Override
            public void write(final short value) {
                Robot.this.hardwareBus.setShutdownLevel(value);
            }
        };
    }
    
    public MineLayer getMineLayer() {
        return this.mineLayer;
    }
    
    public Shield getShield() {
        return this.shield;
    }
    
    public void setTurret(final Turret turret) {
        this.turret = turret;
    }
    
    public void setTransponder(final Transponder transponder) {
        this.transponder = transponder;
    }
    
    public void setTransceiver(final Transceiver transceiver) {
        this.transceiver = transceiver;
    }
    
    public void setArmor(final Armor armor) {
        (this.armor = armor).setArmorDepletionListener(this);
    }
    
    public void setRadar(final Radar radar) {
        this.radar = radar;
    }
    
    public void setSonar(final Sonar sonar) {
        this.sonar = sonar;
    }
    
    public void setMineLayer(final MineLayer mineLayer) {
        this.mineLayer = mineLayer;
    }
    
    public void setShield(final Shield shield) {
        this.shield = shield;
    }
    
    public HardwareBus getHardwareBus() {
        return this.hardwareBus;
    }
    
    public void setHardwareBus(final HardwareBus hardwareBus) {
        this.hardwareBus = hardwareBus;
    }
    
    @Override
    public void reset() {
        this.setOverburn(false);
    }
    
    @Override
    public ScanResult scan(final AngleBracket angleBracket, final double maxDistance, final boolean calculateAccuracy, final boolean includeTargetDetails) {
        final RobotScanResult scanResult = this.doScan(angleBracket, maxDistance, calculateAccuracy);
        if (scanResult.successful()) {
            this.getComputer().getRegisters().getTargetId().set((short)scanResult.getMatch().transponder.getId());
            if (includeTargetDetails) {
                final AbsoluteAngle matchAngle = scanResult.getMatch().getHeading().getAngle();
                final AbsoluteAngle turretAngle = this.getTurret().getHeading().getAngle();
                this.getComputer().getRegisters().getTargetHeading().set((short)matchAngle.getAngleCounterClockwiseTo(turretAngle).getBygrees());
                this.getComputer().getRegisters().getTargetThrottle().set((short)scanResult.getMatch().getThrottle().getPower());
                this.getComputer().getRegisters().getTargetVelocity().set((short)Math.round(scanResult.getMatch().getSpeed().times(Duration.ONE_CYCLE) * 100.0));
            }
        }
        return scanResult;
    }
    
    private RobotScanResult doScan(final AngleBracket angleBracket, final double maxDistance, final boolean calculateAccuracy) {
        final Position position = this.getPosition();
        final RobotScanner robotScanner = new RobotScanner(this, position, angleBracket, maxDistance, calculateAccuracy);
        this.getArena().visitActiveRobots(robotScanner);
        final RobotScanResult scanResult = robotScanner.toScanResult();
        final Scan object = new Scan(angleBracket, maxDistance, scanResult.successful(), scanResult.getMatchPositionVector(), calculateAccuracy && scanResult.successful(), scanResult.getAccuracy());
        this.getArena().addIntangible(object);
        object.getPosition().copyFrom(position);
        return scanResult;
    }
    
    @Override
    protected ArenaObjectSnapshot createSpecificSnapshot() {
        final RobotSnapshot robotSnapshot = new RobotSnapshot();
        robotSnapshot.setTemperature(this.getHeatSinks().getTemperature());
        robotSnapshot.setArmor(this.getArmor().getRemaining());
        robotSnapshot.setOverburn(this.isOverburn());
        robotSnapshot.setActiveShield(this.getShield().isActive());
        robotSnapshot.setHeading(this.getHeading().getAngle());
        robotSnapshot.setTurretHeading(this.getTurret().getHeading().getAngle());
        robotSnapshot.setName(this.getName());
        robotSnapshot.setId(this.getId());
        robotSnapshot.setRoundKills(this.getRoundKills());
        robotSnapshot.setTotalKills(this.getTotalKills());
        robotSnapshot.setTotalDeaths(this.getTotalDeaths());
        robotSnapshot.setTotalWins(this.getTotalWins());
        robotSnapshot.setTotalTies(this.getTotalTies());
        robotSnapshot.setLastMessage(this.getComputer().getLastMessage());
        return robotSnapshot;
    }
    
    @Override
    public void checkCollision(final TangibleArenaObject robot) {
        if (robot.getPosition().getVectorTo(this.position).getMagnitudeSquared() < 64.0) {
            this.collides();
            robot.collides();
        }
    }
    
    @Override
    public void collides() {
        this.position.copyFrom(this.oldPosition);
        if (this.speed.times(Duration.ONE_CYCLE) > 2.0) {
            this.armor.inflictDamage(1.0);
        }
        this.throttle.setPower(0);
        this.throttle.setDesiredPower(0);
        this.computer.getRegisters().getCollisionCount().increment();
    }
    
    @Override
    public void inflictDamage(final DamageInflicter cause, final double damageAmount) {
        if (!this.isDead()) {
            final double unabsorbedAmount = this.shield.absorbDamage(damageAmount);
            if (unabsorbedAmount > 0.0) {
                this.lastDamageTaken = this.getArena().getRoundTimer().getTime();
                cause.inflictedDamage(unabsorbedAmount);
            }
            this.armor.inflictDamage(unabsorbedAmount);
            if (this.isDead()) {
                cause.killedRobot();
            }
        }
    }
    
    @Override
    public void armorDepleted() {
        if (!this.isDead()) {
            for (final RobotListener listener : this.robotListeners) {
                listener.died(this);
            }
            this.die();
            this.getArena().explosion(this, new LinearDamageFunction(this.position, this.isOverburn() ? 1.3 : 1.0, 25.0));
        }
    }
    
    @Override
    public void update(final Duration duration) {
        this.oldPosition.copyFrom(this.position);
        super.update(duration);
        this.getThrottle().update(duration);
        this.getHeading().moveToward(this.getDesiredHeading(), Robot.STEERING_SPEED);
        this.getComputer().update(duration);
        if (this.heatSinks.getTemperature().getLogScale() >= 500.0) {
            this.destruct();
        }
        else if (this.heatSinks.getTemperature().getLogScale() >= 475.0) {
            this.armor.inflictDamage(duration.getCycles() / 4.0);
        }
        else if (this.heatSinks.getTemperature().getLogScale() >= 450.0) {
            this.armor.inflictDamage(duration.getCycles() / 8.0);
        }
        else if (this.heatSinks.getTemperature().getLogScale() >= 400.0) {
            this.armor.inflictDamage(duration.getCycles() / 16.0);
        }
        else if (this.heatSinks.getTemperature().getLogScale() >= 350.0) {
            this.armor.inflictDamage(duration.getCycles() / 32.0);
        }
        else if (this.heatSinks.getTemperature().getLogScale() >= 300.0) {
            this.armor.inflictDamage(duration.getCycles() / 64.0);
        }
        this.heatSinks.cool(this.isOverburn() ? this.getCoolTemp(duration).times(0.66) : this.getCoolTemp(duration));
        this.shield.update(duration);
        if (this.position.getX() < 4.0 || this.position.getX() > 996.0 || this.position.getY() < 4.0 || this.position.getY() > 996.0) {
            this.collides();
        }
    }
    
    private Temperature getCoolTemp(final Duration duration) {
        return Temperature.fromLogScale(duration.getCycles() * 1.125);
    }
    
    public void setThrottle(final Throttle throttle) {
        (this.throttle = throttle).setSpeed(this.speed);
        throttle.setHeatSinks(this.heatSinks);
    }
    
    @Override
    public void winRound() {
        for (final RobotListener listener : this.robotListeners) {
            listener.wonRound(this);
        }
    }
    
    @Override
    public void tieRound() {
        for (final RobotListener listener : this.robotListeners) {
            listener.tiedRound(this);
        }
    }
    
    @Override
    public void killedRobot() {
        for (final RobotListener listener : this.robotListeners) {
            listener.killedRobot(this);
        }
    }
    
    @Override
    public void inflictedDamage(final double amount) {
        this.lastDamageGiven = this.getArena().getRoundTimer().getTime();
        for (final RobotListener listener : this.robotListeners) {
            listener.inflictedDamage(this, amount);
        }
    }
    
    public String getName() {
        return this.name;
    }
    
    public int getTotalKills() {
        return this.score.getTotalKills();
    }
    
    public int getRoundKills() {
        return this.roundKills;
    }
    
    public int getTotalDeaths() {
        return this.score.getTotalDeaths();
    }
    
    public int getId() {
        return this.id;
    }
    
    public int getTotalWins() {
        return this.score.getTotalWins();
    }
    
    public int getTotalTies() {
        return this.score.getTotalTies();
    }
    
    public void addRobotListener(final RobotListener robotListener) {
        this.robotListeners.add(robotListener);
    }
    
    @Override
    protected void arenaConnected(final Arena arena) {
        this.transceiver.setRadioDispatcher(arena.getRadioDispatcher());
    }
    
    public InterruptHandler createGetRobotInfoInterruptHandler(final MemoryCell speed, final MemoryCell lastDamageTaken, final MemoryCell lastDamageGiven) {
        return new GetRobotInfoInterrupt(speed, lastDamageTaken, lastDamageGiven);
    }
    
    public GetRobotStatisticsInterrupt createGetRobotStatisticsInterrupt(final MemoryCell totalKills, final MemoryCell roundKills, final MemoryCell totalDeaths) {
        return new GetRobotStatisticsInterrupt(totalKills, roundKills, totalDeaths);
    }
    
    @Override
    public Missile createMissile(final AbsoluteAngle heading, final Position position, final double power) {
        return new Missile(this, position, heading, power, this.isOverburn());
    }
    
    @Override
    public void accept(final ArenaObjectVisitor arenaObjectVisitor) {
        arenaObjectVisitor.visit(this);
    }
    
    static {
        STEERING_SPEED = RelativeAngle.fromBygrees(8);
    }
    
    public class GetRobotInfoInterrupt extends InterruptHandler
    {
        private final MemoryCell speed;
        private final MemoryCell lastDamageTaken;
        private final MemoryCell lastDamageGiven;
        
        private GetRobotInfoInterrupt(final MemoryCell speed, final MemoryCell lastDamageTaken, final MemoryCell lastDamageGiven) {
            this.speed = speed;
            this.lastDamageTaken = lastDamageTaken;
            this.lastDamageGiven = lastDamageGiven;
        }
        
        public void handleInterrupt() {
            this.speed.set((short)Math.round(Robot.this.getSpeed().times(Duration.ONE_CYCLE) * 100.0));
            this.lastDamageGiven.set((short)Robot.this.getLastDamageGiven().getCycles());
            this.lastDamageTaken.set((short)Robot.this.getLastDamageTaken().getCycles());
        }
    }
    
    public class GetRobotStatisticsInterrupt extends InterruptHandler
    {
        private final MemoryCell totalKills;
        private final MemoryCell roundKills;
        private final MemoryCell totalDeaths;
        
        private GetRobotStatisticsInterrupt(final MemoryCell totalKills, final MemoryCell roundKills, final MemoryCell totalDeaths) {
            this.totalKills = totalKills;
            this.roundKills = roundKills;
            this.totalDeaths = totalDeaths;
        }
        
        public void handleInterrupt() {
            this.totalKills.set((short)Robot.this.getTotalKills());
            this.roundKills.set((short)Robot.this.getRoundKills());
            this.totalDeaths.set((short)Robot.this.getTotalDeaths());
        }
    }
}
