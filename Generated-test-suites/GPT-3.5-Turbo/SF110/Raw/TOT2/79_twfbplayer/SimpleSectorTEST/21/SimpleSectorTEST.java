import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class SimpleSectorTest {

    private SimpleSector simpleSector;
    private CombatantSide mockCombatantSide;
    
    @Before
    public void setUp() {
        mockCombatantSide = Mockito.mock(CombatantSide.class);
        simpleSector = new SimpleSector(10, true, false, 5, 3, false, 2, CharacterClass.WARRIOR);
    }
    
    @Test
    public void testFree() {
        simpleSector.gainControl(mockCombatantSide);
        simpleSector.free();
        assertEquals("Occupier should be null", null, simpleSector.getOccupier());
    }
    
    @Test
    public void testGainControl() {
        simpleSector.gainControl(mockCombatantSide);
        assertTrue("Sector should be occupied", simpleSector.isOccupied());
    }
    
    @Test
    public void testGetHeight() {
        assertEquals("Height should be 10", 10, simpleSector.getHeight());
    }
    
    @Test
    public void testIsDefenderSpawn() {
        assertTrue("Should be a defender spawn", simpleSector.isDefenderSpawn());
    }
    
    @Test
    public void testIsAttackerSpawn() {
        assertFalse("Should not be an attacker spawn", simpleSector.isAttackerSpawn());
    }
    
    @Test
    public void testIsFlag() {
        assertFalse("Should not have a flag", simpleSector.isFlag());
    }
    
    @Test
    public void testGetBonus() {
        SectorBonus bonus = simpleSector.getBonus(CharacterClass.WARRIOR);
        assertNotNull("Bonus should not be null", bonus);
    }
    
    @Test
    public void testEquals() {
        SimpleSector other = new SimpleSector(10, true, false, 5, 3, false, 2, CharacterClass.WARRIOR);
        assertTrue("Sectors should be equal", simpleSector.equals(other));
    }
    
    @Test
    public void testAddArea() {
        Area mockArea = Mockito.mock(Area.class);
        simpleSector._addArea(mockArea);
        assertTrue("Area should be added", simpleSector.getAreas().contains(mockArea));
    }
}