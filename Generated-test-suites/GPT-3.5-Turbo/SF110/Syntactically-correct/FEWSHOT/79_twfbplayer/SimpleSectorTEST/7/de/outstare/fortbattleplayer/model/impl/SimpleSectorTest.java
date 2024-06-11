package de.outstare.fortbattleplayer.model.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import de.outstare.fortbattleplayer.model.CharacterClass;
import de.outstare.fortbattleplayer.model.CombatantSide;
import de.outstare.fortbattleplayer.model.Sector;
import de.outstare.fortbattleplayer.model.SectorBonus;
import de.outstare.fortbattleplayer.model.SectorObserver;
import de.outstare.fortbattleplayer.model.Weapon;

public class SimpleSectorTest {
    
    private SimpleSector sector;
    private final int height = 5;
    private final boolean defenderSpawn = true;
    private final boolean attackerSpawn = true;
    private final int attackBonus = 10;
    private final int defendBonus = 5;
    private final boolean flag = true;
    private final int classBonus = 2;
    private final CharacterClass classType = CharacterClass.WARRIOR;
    
    @Before
    public void setup() {
        sector = new SimpleSector(height, defenderSpawn, attackerSpawn, attackBonus, defendBonus, flag, classBonus, classType);
    }
    
    @Test
    public void testFree() {
        sector.occupierCount = 2;
        sector.free();
        assertEquals(1, sector.occupierCount);
        
        sector.occupierCount = 1;
        sector.free();
        assertNull(sector.occupier);
        assertEquals(0, sector.occupierCount);
    }
    
    @Test
    public void testOccupierChanged() {
        sector.observers.add(new SectorObserver() {
            @Override
            public void occupierChanged() {
                // do nothing
            }
        });
        sector.occupierChanged();
        // assert that no exception occurs
        
        sector.observers.clear();
        sector.occupierChanged();
        // assert that no exception occurs
    }
    
    @Test
    public void testGainControl() {
        CombatantSide side = CombatantSide.ATTACKER;
        sector.occupierCount = 0;
        sector.gainControl(side);
        assertEquals(side, sector.occupier);
        assertEquals(1, sector.occupierCount);
        
        side = CombatantSide.DEFENDER;
        sector.gainControl(side);
        assertEquals(side, sector.occupier);
        assertEquals(2, sector.occupierCount);
    }
    
    @Test
    public void testGetOccupier() {
        assertNull(sector.getOccupier());
        
        CombatantSide side = CombatantSide.ATTACKER;
        sector.occupier = side;
        assertEquals(side, sector.getOccupier());
        
        side = CombatantSide.DEFENDER;
        sector.occupier = side;
        assertEquals(side, sector.getOccupier());
    }
    
    @Test
    public void testIsOccupied() {
        assertFalse(sector.isOccupied());
        
        sector.occupier = CombatantSide.ATTACKER;
        assertTrue(sector.isOccupied());
        
        sector.occupier = CombatantSide.DEFENDER;
        assertTrue(sector.isOccupied());
    }
    
    @Test
    public void testGetHeight() {
        assertEquals(height, sector.getHeight());
    }
    
    @Test
    public void testIsDefenderSpawn() {
        assertTrue(sector.isDefenderSpawn());
    }
    
    @Test
    public void testIsAttackerSpawn() {
        assertTrue(sector.isAttackerSpawn());
    }
    
    @Test
    public void testIsFlag() {
        assertTrue(sector.isFlag());
    }
    
    @Test
    public void testGetBonus() {
        final SectorBonus bonus = sector.getBonus(CharacterClass.WARRIOR);
        assertEquals(12, bonus.getAttackBonus());
        assertEquals(7, bonus.getDefenseBonus());
        assertEquals(0, bonus.getDamageBonus());
    }
    
    @Test
    public void testGetDamageBonus() {
        final int bonus = sector.getDamageBonus();
        assertEquals(0, bonus);
    }
    
    @Test
    public void testGetBonusFromWeapons() {
        // create a sector with an area occupied by a combatant with a weapon
        final SimpleSector s = new SimpleSector(height, defenderSpawn, attackerSpawn, attackBonus, defendBonus, flag, classBonus, classType);
        final Area area = new SimpleArea(null);
        final Combatant combatant = new SimpleCombatant(null);
        final Weapon weapon = new SimpleWeapon();
        weapon.setSectorBonus(5, SectorBonus.BonusType.DAMAGE);
        combatant.setWeapon(weapon);
        area.setOccupier(combatant);
        s._addArea(area);
        
        final int bonus = s.getBonusFromWeapons(SectorBonus.BonusType.DAMAGE);
        assertEquals(5, bonus);
    }
    
    @Test
    public void testGetAttackBonus() {
        final int bonus = sector.getAttackBonus(CharacterClass.WARRIOR);
        assertEquals(12, bonus);
    }
    
    @Test
    public void testGetDefenseBonus() {
        final int bonus = sector.getDefenseBonus(CharacterClass.WARRIOR);
        assertEquals(7, bonus);
    }
    
    @Test
    public void testClassBonus() {
        int bonus = sector.classBonus(CharacterClass.WARRIOR);
        assertEquals(2, bonus);
        
        bonus = sector.classBonus(CharacterClass.ARCHER);
        assertEquals(0, bonus);
    }
    
    @Test
    public void testGetAreas() {
        assertTrue(sector.getAreas().isEmpty());
        
        final Area area = new SimpleArea(null);
        sector._addArea(area);
        assertTrue(sector.getAreas().contains(area));
    }
    
    @Test
    public void testAddObserver() {
        sector.addObserver(new SectorObserver() {
            @Override
            public void occupierChanged() {
                // do nothing
            }
        });
        // assert that no exception occurs
    }
    
    @Test
    public void testRemoveObserver() {
        sector.removeObserver(new SectorObserver() {
            @Override
            public void occupierChanged() {
                // do nothing
            }
        });
        // assert that no exception occurs
    }
    
    @Test
    public void testHashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((sector.getAreas() == null) ? 0 : sector.getAreas().hashCode());
        result = prime * result + sector.getAttackBonus(CharacterClass.WARRIOR);
        result = prime * result + (sector.isAttackerSpawn() ? 1231 : 1237);
        result = prime * result + sector.classBonus(CharacterClass.WARRIOR);
        result = prime * result + sector.getDefenseBonus(CharacterClass.WARRIOR);
        result = prime * result + (sector.isDefenderSpawn() ? 1231 : 1237);
        result = prime * result + (sector.isFlag() ? 1231 : 1237);
        result = prime * result + sector.getHeight();
        assertEquals(result, sector.hashCode());
    }
    
    @Test
    public void testEquals() {
        assertFalse(sector.equals(null));
        assertFalse(sector.equals(new SimpleSector()));
        assertTrue(sector.equals(sector));
        
        final SimpleSector other = new SimpleSector();
        assertFalse(sector.equals(other));
        assertFalse(other.equals(sector));
        
        other.height = height;
        assertFalse(sector.equals(other));
        assertFalse(other.equals(sector));
        
        other.defenderSpawn = defenderSpawn;
        assertFalse(sector.equals(other));
        assertFalse(other.equals(sector));
        
        other.attackerSpawn = attackerSpawn;
        assertFalse(sector.equals(other));
        assertFalse(other.equals(sector));
        
        other.attackBonus = attackBonus;
        assertFalse(sector.equals(other));
        assertFalse(other.equals(sector));
        
        other.defendBonus = defendBonus;
        assertFalse(sector.equals(other));
        assertFalse(other.equals(sector));
        
        other.flag = flag;
        assertFalse(sector.equals(other));
        assertFalse(other.equals(sector));
        
        other.classBonus = classBonus;
        assertFalse(sector.equals(other));
        assertFalse(other.equals(sector));
        
        other.classType = classType;
        assertFalse(sector.equals(other));
        assertFalse(other.equals(sector));
    }
    
    class SimpleArea implements Area {

        private Combatant occupier;

        public SimpleArea(Combatant occupier) {
            this.occupier = occupier;
        }

        @Override
        public boolean isOccupied() {
            return occupier != null;
        }

        @Override
        public Combatant getOccupier() {
            return occupier;
        }

        @Override
        public void setOccupier(Combatant occupier) {
            this.occupier = occupier;
        }
        
    }
    
    class SimpleCombatant implements Combatant {

        private Weapon weapon;

        public SimpleCombatant(Weapon weapon) {
            this.weapon = weapon;
        }

        @Override
        public Weapon getWeapon() {
            return weapon;
        }

        @Override
        public void setWeapon(Weapon weapon) {
            this.weapon = weapon;
        }
        
    }
    
    class SimpleWeapon implements Weapon {
        private int sectorBonus;

        @Override
        public int getSectorBonus(SectorBonus.BonusType type) {
            return sectorBonus;
        }

        public void setSectorBonus(int sectorBonus, SectorBonus.BonusType type) {
            this.sectorBonus = sectorBonus;
        }
    }
}