import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import de.outstare.fortbattleplayer.model.*;
import de.outstare.fortbattleplayer.model.impl.SimpleSector;

public class SimpleSectorTest {
    
    private Sector sector;
    
    @Before
    public void setUp() {
        sector = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
    }
    
    @Test
    public void testFree() {
        sector.gainControl(CombatantSide.ATTACKER);
        sector.free();
        assertFalse(sector.isOccupied());
    }
    
    @Test
    public void testGainControl() {
        sector.gainControl(CombatantSide.DEFENDER);
        assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
    }
    
    @Test
    public void testGetOccupier() {
        sector.gainControl(CombatantSide.DEFENDER);
        assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
    }
    
    @Test
    public void testIsOccupied() {
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
    }
    
    @Test
    public void testGetAreas() {
        assertNotNull(sector.getAreas());
    }
    
    @Test
    public void test_addArea() {
        Area area = new SimpleArea();
        sector._addArea(area);
        assertTrue(sector.getAreas().contains(area));
    }
    
    @Test
    public void testHashCode() {
        int hashCode = sector.hashCode();
        assertNotNull(hashCode);
    }
    
    @Test
    public void testEquals() {
        SimpleSector other = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
        assertTrue(sector.equals(other));
    }
    
    @Test
    public void testAddObserver() {
        SectorObserver observer = new SimpleSectorObserver();
        sector.addObserver(observer);
        assertTrue(sector.getObservers().contains(observer));
    }
    
    @Test
    public void testRemoveObserver() {
        SectorObserver observer = new SimpleSectorObserver();
        sector.addObserver(observer);
        sector.removeObserver(observer);
        assertFalse(sector.getObservers().contains(observer));
    }
}