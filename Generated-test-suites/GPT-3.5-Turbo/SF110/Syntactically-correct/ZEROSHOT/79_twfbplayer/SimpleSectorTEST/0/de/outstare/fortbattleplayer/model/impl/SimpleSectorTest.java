import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

public class SimpleSectorTest {
    
    @Test
    public void testFree() {
        SimpleSector sector = new SimpleSector(5, true, false, 10, 20, true, 5, CharacterClass.WARRIOR);
        sector.free();
        assertEquals(0, sector.getOccupierCount());
        assertNull(sector.getOccupier());
    }
    
    @Test
    public void testGainControl() {
        SimpleSector sector = new SimpleSector(5, true, false, 10, 20, true, 5, CharacterClass.WARRIOR);
        sector.gainControl(CombatantSide.ATTACKER);
        assertEquals(1, sector.getOccupierCount());
        assertEquals(CombatantSide.ATTACKER, sector.getOccupier());
    }
    
    @Test
    public void testGetOccupier() {
        SimpleSector sector = new SimpleSector(5, true, false, 10, 20, true, 5, CharacterClass.WARRIOR);
        sector.gainControl(CombatantSide.DEFENDER);
        assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
    }
    
    @Test
    public void testIsOccupied() {
        SimpleSector sector = new SimpleSector(5, true, false, 10, 20, true, 5, CharacterClass.WARRIOR);
        assertFalse(sector.isOccupied());
        
        sector.gainControl(CombatantSide.DEFENDER);
        assertTrue(sector.isOccupied());
    }
    
    @Test
    public void testGetHeight() {
        SimpleSector sector = new SimpleSector(5, true, false, 10, 20, true, 5, CharacterClass.WARRIOR);
        assertEquals(5, sector.getHeight());
    }
    
    @Test
    public void testIsDefenderSpawn() {
        SimpleSector sector = new SimpleSector(5, true, false, 10, 20, true, 5, CharacterClass.WARRIOR);
        assertTrue(sector.isDefenderSpawn());
    }
    
    @Test
    public void testIsAttackerSpawn() {
        SimpleSector sector = new SimpleSector(5, true, false, 10, 20, true, 5, CharacterClass.WARRIOR);
        assertFalse(sector.isAttackerSpawn());
    }
    
    @Test
    public void testIsFlag() {
        SimpleSector sector = new SimpleSector(5, true, false, 10, 20, true, 5, CharacterClass.WARRIOR);
        assertTrue(sector.isFlag());
    }
    
    @Test
    public void testGetBonus() {
        SimpleSector sector = new SimpleSector(5, true, false, 10, 20, true, 5, CharacterClass.WARRIOR);
        SectorBonus bonus = sector.getBonus(CharacterClass.WARRIOR);
        assertNotNull(bonus);
        assertEquals(25, bonus.getAttack());
        assertEquals(25, bonus.getDefense());
        assertEquals(0, bonus.getDamage());
    }
    
    @Test
    public void testGetAreas() {
        SimpleSector sector = new SimpleSector(5, true, false, 10, 20, true, 5, CharacterClass.WARRIOR);
        Set<Area> areas = sector.getAreas();
        assertNotNull(areas);
    }
    
    @Test
    public void testAddArea() {
        SimpleSector sector = new SimpleSector(5, true, false, 10, 20, true, 5, CharacterClass.WARRIOR);
        Area area = new Area();
        sector._addArea(area);
        assertTrue(sector.getAreas().contains(area));
    }
    
    @Test
    public void testHashCode() {
        SimpleSector sector1 = new SimpleSector(5, true, false, 10, 20, true, 5, CharacterClass.WARRIOR);
        SimpleSector sector2 = new SimpleSector(5, true, false, 10, 20, true, 5, CharacterClass.WARRIOR);
        assertEquals(sector1.hashCode(), sector2.hashCode());
    }
    
    @Test
    public void testEquals() {
        SimpleSector sector1 = new SimpleSector(5, true, false, 10, 20, true, 5, CharacterClass.WARRIOR);
        SimpleSector sector2 = new SimpleSector(5, true, false, 10, 20, true, 5, CharacterClass.WARRIOR);
        assertTrue(sector1.equals(sector2));
    }
    
    @Test
    public void testAddObserver() {
        SimpleSector sector = new SimpleSector(5, true, false, 10, 20, true, 5, CharacterClass.WARRIOR);
        SectorObserver observer = new SectorObserver() {
            public void occupierChanged() {
                // do nothing
            }
        };
        sector.addObserver(observer);
        assertTrue(sector.getObservers().contains(observer));
    }
    
    @Test
    public void testRemoveObserver() {
        SimpleSector sector = new SimpleSector(5, true, false, 10, 20, true, 5, CharacterClass.WARRIOR);
        SectorObserver observer = new SectorObserver() {
            public void occupierChanged() {
                // do nothing
            }
        };
        sector.addObserver(observer);
        sector.removeObserver(observer);
        assertFalse(sector.getObservers().contains(observer));
    }
}