package de.outstare.fortbattleplayer.model.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import de.outstare.fortbattleplayer.model.Area;
import de.outstare.fortbattleplayer.model.CharacterClass;
import de.outstare.fortbattleplayer.model.Combatant;
import de.outstare.fortbattleplayer.model.CombatantSide;
import de.outstare.fortbattleplayer.model.Sector;
import de.outstare.fortbattleplayer.model.SectorBonus;
import de.outstare.fortbattleplayer.model.SectorObserver;
import de.outstare.fortbattleplayer.model.Weapon;

public class SimpleSectorTest {
    private SimpleSector sector;
    private Set<Area> areas;
    private SectorObserver observer;

    @Before
    public void setUp() {
        areas = new HashSet<>();
        observer = () -> {};
        sector = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.SOLDIER);
        sector._addArea(new Area() {
            @Override
            public boolean isOccupied() {
                return false;
            }
            
            @Override
            public Combatant getOccupier() {
                return null;
            }
        });
        sector.addObserver(observer);
    }

    @Test
    public void testFree() {
        sector.gainControl(CombatantSide.ATTACKER);
        sector.free();
        assertFalse(sector.isOccupied());
    }
    
    @Test
    public void testOccupierChanged() {
        SimpleSectorMock observer = new SimpleSectorMock();
        sector.addObserver(observer);
        sector.gainControl(CombatantSide.ATTACKER);
        assertTrue(observer.occupierChanged);
    }
    
    @Test
    public void testGainControl() {
        sector.gainControl(CombatantSide.ATTACKER);
        assertTrue(sector.isOccupied());
        assertEquals(CombatantSide.ATTACKER, sector.getOccupier());
    }
    
    @Test
    public void testGetHeight() {
        assertEquals(10, sector.getHeight());
    }
    
    @Test
    public void testIsDefenderSpawn() {
        assertTrue(sector.isDefenderSpawn());
    }
    
    @Test
    public void testIsAttackerSpawn() {
        assertFalse(sector.isAttackerSpawn());
    }
    
    @Test
    public void testIsFlag() {
        assertTrue(sector.isFlag());
    }
    
    @Test
    public void testGetBonus() {
        SectorBonus bonus = sector.getBonus(CharacterClass.SOLDIER);
        assertEquals(7, bonus.getAttackBonus());
        assertEquals(5, bonus.getDefenseBonus());
        assertEquals(0, bonus.getDamageBonus());
    }
    
    @Test
    public void testGetBonusFromWeapons() {
        sector._addArea(new Area() {
            @Override
            public boolean isOccupied() {
                return true;
            }
            
            @Override
            public Combatant getOccupier() {
                return new Combatant() {
                    @Override
                    public Weapon getWeapon() {
                        return new Weapon() {
                            @Override
                            public int getSectorBonus(BonusType type) {
                                if (type == BonusType.DAMAGE) {
                                    return 3;
                                } else {
                                    return 0;
                                }
                            }
                        };
                    }
                };
            }
        });
        SectorBonus bonus = sector.getBonus(CharacterClass.SOLDIER);
        assertEquals(7, bonus.getAttackBonus());
        assertEquals(5, bonus.getDefenseBonus());
        assertEquals(3, bonus.getDamageBonus());
    }
    
    @Test
    public void testGetAttackBonus() {
        assertEquals(7, sector.getAttackBonus(CharacterClass.SOLDIER));
    }
    
    @Test
    public void testGetDefenseBonus() {
        assertEquals(5, sector.getDefenseBonus(CharacterClass.SOLDIER));
    }
    
    @Test
    public void testClassBonus() {
        assertEquals(2, sector.classBonus(CharacterClass.SOLDIER));
    }
    
    @Test
    public void testGetAreas() {
        Set<Area> areas = sector.getAreas();
        assertEquals(1, areas.size());
    }
    
    @Test
    public void test_addArea() {
        Area area = new Area() {
            @Override
            public boolean isOccupied() {
                return false;
            }
            
            @Override
            public Combatant getOccupier() {
                return null;
            }
        };
        sector._addArea(area);
        Set<Area> areas = sector.getAreas();
        assertEquals(2, areas.size());
        assertTrue(areas.contains(area));
    }
    
    @Test
    public void testHashCode() {
        int expectedHash = 31 + 31 * (31 * (31 * (31 * (31 * (31 * (31 * (31 * 1 + areas.hashCode()) + 5) + (true ? 1231 : 1237)) + 2) + 0) + CharacterClass.SOLDIER.hashCode()) + 3) + (true ? 1231 : 1237);
        assertEquals(expectedHash, sector.hashCode());
    }
    
    @Test
    public void testEquals() {
        SimpleSector other = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.SOLDIER);
        assertEquals(sector, other);
    }
    
    @Test
    public void testAddObserver() {
        SimpleSectorMock observer = new SimpleSectorMock();
        sector.addObserver(observer);
        sector.gainControl(CombatantSide.ATTACKER);
        assertTrue(observer.occupierChanged);
    }
    
    @Test
    public void testRemoveObserver() {
        SimpleSectorMock observer = new SimpleSectorMock();
        sector.addObserver(observer);
        sector.removeObserver(observer);
        sector.gainControl(CombatantSide.ATTACKER);
        assertFalse(observer.occupierChanged);
    }
    
    private static class SimpleSectorMock implements SectorObserver {
        boolean occupierChanged = false;
        
        @Override
        public void occupierChanged() {
            occupierChanged = true;
        }
    }
}