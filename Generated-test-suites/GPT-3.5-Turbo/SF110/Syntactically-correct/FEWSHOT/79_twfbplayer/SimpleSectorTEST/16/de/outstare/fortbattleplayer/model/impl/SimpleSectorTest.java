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
import de.outstare.fortbattleplayer.model.SectorBonus;
import de.outstare.fortbattleplayer.model.SectorObserver;
import de.outstare.fortbattleplayer.model.Weapon;

public class SimpleSectorTest {
    private SimpleSector sector;
    private Set<Area> areas;
    
    @Before
    public void setUp() {
    	areas = new HashSet<>();
        sector = new SimpleSector(1, true, false, 5, 10, true, 15, CharacterClass.WARRIOR);
        sector._addArea(new Area() {
			public boolean isOccupied() {
				return true;
			}
			
			public Combatant getOccupier() {
				Combatant combatant = new Combatant() {
					public Weapon getWeapon() {
						return new Weapon() {
							public int getSectorBonus(SectorBonus.BonusType type) {
								return 5;
							}
						};
					}
				};
				return combatant;
			}
			
		});
        sector._addArea(new Area() {
			public boolean isOccupied() {
				return false;
			}
		});
        sector._addArea(new Area() {
			public boolean isOccupied() {
				return true;
			}
			
			public Combatant getOccupier() {
				Combatant combatant = new Combatant() {
					public Weapon getWeapon() {
						return new Weapon() {
							public int getSectorBonus(SectorBonus.BonusType type) {
								return 0;
							}
						};
					}
				};
				return combatant;
			}
			
		});
    }
    
    @Test
    public void testFree() {
        sector.occupierCount = 1;
        sector.free();
        assertEquals(0, sector.occupierCount);
        assertNull(sector.occupier);
        
        sector.occupierCount = 0;
        sector.free();
        assertEquals(0, sector.occupierCount);
        assertNull(sector.occupier);
    }
    
    @Test
    public void testOccupierChanged() {
        SimpleSectorObserverImpl observer1 = new SimpleSectorObserverImpl();
        SimpleSectorObserverImpl observer2 = new SimpleSectorObserverImpl();
        
        sector.addObserver(observer1);
        sector.addObserver(observer2);
        
        sector.occupierChanged();
        
        assertTrue(observer1.occupierChangedCalled);
        assertTrue(observer2.occupierChangedCalled);
    }
    
    @Test
    public void testGainControl() {
        sector.gainControl(CombatantSide.DEFENDER);
        assertEquals(1, sector.occupierCount);
        assertEquals(CombatantSide.DEFENDER, sector.occupier);
    }
    
    @Test
    public void testGetOccupier() {
        assertNull(sector.getOccupier());
        sector.gainControl(CombatantSide.ATTACKER);
        assertEquals(CombatantSide.ATTACKER, sector.getOccupier());
    }
    
    @Test
    public void testIsOccupied() {
        assertFalse(sector.isOccupied());
        sector.gainControl(CombatantSide.DEFENDER);
        assertTrue(sector.isOccupied());
    }
    
    @Test
    public void testGetHeight() {
        assertEquals(1, sector.getHeight());
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
        SectorBonus bonus = sector.getBonus(CharacterClass.WARRIOR);
        assertEquals(20, bonus.getAttackBonus());
        assertEquals(25, bonus.getDefenseBonus());
        assertEquals(5, bonus.getDamageBonus());
    }
    
    @Test
    public void testGetAreas() {
        assertEquals(3, sector.getAreas().size());
    }
    
    @Test
    public void testHashCode() {
        SimpleSector sector2 = new SimpleSector(1, true, false, 5, 10, true, 15, CharacterClass.WARRIOR);
        assertEquals(sector.hashCode(), sector2.hashCode());
    }
    
    @Test
    public void testEquals() {
        SimpleSector sector2 = new SimpleSector(1, true, false, 5, 10, true, 15, CharacterClass.WARRIOR);
        assertEquals(sector, sector2);
    }
    
    @Test
    public void testAddObserver() {
        SimpleSectorObserverImpl observer = new SimpleSectorObserverImpl();
        
        sector.addObserver(observer);
        sector.occupierChanged();
        
        assertTrue(observer.occupierChangedCalled);
    }
    
    @Test
    public void testRemoveObserver() {
        SimpleSectorObserverImpl observer = new SimpleSectorObserverImpl();
        
        sector.addObserver(observer);
        sector.removeObserver(observer);
        sector.occupierChanged();
        
        assertFalse(observer.occupierChangedCalled);
    }
    
    private class SimpleSectorObserverImpl implements SectorObserver {
        public boolean occupierChangedCalled = false;
        
        @Override
        public void occupierChanged() {
            occupierChangedCalled = true;
        }
        
    }
}