import static org.junit.Assert.*;
import org.junit.*;
import java.util.*;

public class SimpleSectorTest {
    
    private SimpleSector simpleSector;
    
    @Before
    public void setUp() {
        simpleSector = new SimpleSector(5, true, false, 10, 20, true, 5, CharacterClass.WARRIOR);
    }
    
    @Test
    public void testFree() {
        simpleSector.gainControl(CombatantSide.DEFENDER);
        simpleSector.free();
        assertEquals(0, simpleSector.getOccupierCount());
        assertNull(simpleSector.getOccupier());
    }
    
    @Test
    public void testGainControl() {
        simpleSector.gainControl(CombatantSide.ATTACKER);
        assertEquals(1, simpleSector.getOccupierCount());
        assertEquals(CombatantSide.ATTACKER, simpleSector.getOccupier());
    }
    
    @Test
    public void testIsOccupied() {
        assertFalse(simpleSector.isOccupied());
        simpleSector.gainControl(CombatantSide.DEFENDER);
        assertTrue(simpleSector.isOccupied());
    }
    
    @Test
    public void testGetHeight() {
        assertEquals(5, simpleSector.getHeight());
    }
    
    @Test
    public void testIsDefenderSpawn() {
        assertTrue(simpleSector.isDefenderSpawn());
    }
    
    @Test
    public void testIsAttackerSpawn() {
        assertFalse(simpleSector.isAttackerSpawn());
    }
    
    @Test
    public void testIsFlag() {
        assertTrue(simpleSector.isFlag());
    }
    
    @Test
    public void testGetBonus() {
        SectorBonus bonus = simpleSector.getBonus(CharacterClass.WARRIOR);
        assertNotNull(bonus);
    }
    
    @Test
    public void testGetAreas() {
        Set<Area> areas = simpleSector.getAreas();
        assertNotNull(areas);
    }
    
    @Test
    public void testHashCode() {
        int code = simpleSector.hashCode();
        assertNotEquals(0, code);
    }
    
    @Test
    public void testEquals() {
        SimpleSector other = new SimpleSector(5, true, false, 10, 20, true, 5, CharacterClass.WARRIOR);
        assertTrue(simpleSector.equals(other));
        
        // Test for inequality
        other = new SimpleSector(10, false, false, 30, 40, false, 15, CharacterClass.WIZARD);
        assertFalse(simpleSector.equals(other));
    }
    
    @Test
    public void testAddObserver() {
        SectorObserver observer = new SectorObserver() {
            public void occupierChanged() {
                // do something
            }
        };
        
        simpleSector.addObserver(observer);
        assertTrue(simpleSector.getObservers().contains(observer));
    }
    
    @Test
    public void testRemoveObserver() {
        SectorObserver observer = new SectorObserver() {
            public void occupierChanged() {
                // do something
            }
        };
        
        simpleSector.addObserver(observer);
        simpleSector.removeObserver(observer);
        assertFalse(simpleSector.getObservers().contains(observer));
    }
    
    @After
    public void tearDown() {
        simpleSector = null;
    }
}