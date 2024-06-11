import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.*;
import de.outstare.fortbattleplayer.model.*;

public class SimpleSectorTest {

    private SimpleSector sector;
    
    @Before
    public void setUp() {
        sector = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
    }
    
    @Test
    public void testFree() {
        sector.free();
        assertFalse(sector.isOccupied());
    }
    
    @Test
    public void testGainControl() {
        sector.gainControl(CombatantSide.ATTACKER);
        assertTrue(sector.isOccupied());
        assertEquals(CombatantSide.ATTACKER, sector.getOccupier());
    }
    
    @Test
    public void testGetOccupier() {
        assertNull(sector.getOccupier());
        
        sector.gainControl(CombatantSide.DEFENDER);
        assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
    }
    
    @Test
    public void testIsOccupied() {
        assertFalse(sector.isOccupied());
        
        sector.gainControl(CombatantSide.DEFENDER);
        assertTrue(sector.isOccupied());
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
        SectorBonus bonus = sector.getBonus(CharacterClass.WARRIOR);
        assertNotNull(bonus);
        assertEquals(7, bonus.getAttackBonus());
        assertEquals(5, bonus.getDefenseBonus());
        assertEquals(0, bonus.getDamageBonus());
    }
    
    @Test
    public void testGetAttackBonus() {
        int attackBonus = sector.getAttackBonus(CharacterClass.WARRIOR);
        assertEquals(7, attackBonus);
    }
    
    @Test
    public void testGetDefenseBonus() {
        int defenseBonus = sector.getDefenseBonus(CharacterClass.WARRIOR);
        assertEquals(5, defenseBonus);
    }
    
    @Test
    public void testClassBonus() {
        int classBonus = sector.classBonus(CharacterClass.WARRIOR);
        assertEquals(2, classBonus);
        
        classBonus = sector.classBonus(CharacterClass.ARCHER);
        assertEquals(0, classBonus);
    }
    
    @Test
    public void testGetAreas() {
        Set<Area> areas = sector.getAreas();
        assertNotNull(areas);
        assertEquals(0, areas.size());
    }
    
    @Test
    public void test_addArea() {
        Area area = new Area();
        sector._addArea(area);
        
        Set<Area> areas = sector.getAreas();
        assertNotNull(areas);
        assertEquals(1, areas.size());
        assertTrue(areas.contains(area));
    }
    
    @Test
    public void testHashCode() {
        int hashCode1 = sector.hashCode();
        int hashCode2 = sector.hashCode();
        
        assertEquals(hashCode1, hashCode2);
    }
    
    @Test
    public void testEquals() {
        SimpleSector sector2 = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
        assertEquals(sector, sector2);
        
        SimpleSector sector3 = new SimpleSector(20, true, false, 7, 4, true, 3, CharacterClass.MAGE);
        assertNotEquals(sector, sector3);
    }
    
    @Test
    public void testAddObserver() {
        SectorObserver observer = new SectorObserver() {
            @Override
            public void occupierChanged() {
                // Do nothing
            }
        };
        
        sector.addObserver(observer);
        assertTrue(sector.removeObserver(observer));
    }
}