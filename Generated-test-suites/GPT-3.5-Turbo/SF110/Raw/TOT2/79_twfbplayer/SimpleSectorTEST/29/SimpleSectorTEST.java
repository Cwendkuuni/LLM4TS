import static org.junit.Assert.*;
import org.junit.*;
import java.util.*;

public class SimpleSectorTest {

    private SimpleSector simpleSector;

    @Before
    public void setUp() {
        simpleSector = new SimpleSector(10, true, false, 20, 15, true, 5, CharacterClass.WARRIOR);
    }

    @Test
    public void testFree() {
        simpleSector.gainControl(CombatantSide.DEFENDER);
        simpleSector.free();
        assertFalse(simpleSector.isOccupied());
    }

    @Test
    public void testGainControl() {
        simpleSector.gainControl(CombatantSide.ATTACKER);
        assertEquals(1, simpleSector.getOccupierCount());
        assertEquals(CombatantSide.ATTACKER, simpleSector.getOccupier());
    }

    @Test
    public void testGetBonus() {
        SectorBonus sectorBonus = simpleSector.getBonus(CharacterClass.WIZARD);
        assertNotNull(sectorBonus);
    }
    
    @Test
    public void testGetAttackBonus() {
        int attackBonus = simpleSector.getAttackBonus(CharacterClass.WARRIOR);
        assertEquals(25, attackBonus);
    }

    @Test
    public void testGetDefenseBonus() {
        int defenseBonus = simpleSector.getDefenseBonus(CharacterClass.WARRIOR);
        assertEquals(20, defenseBonus);
    }

    @Test
    public void testEquals() {
        SimpleSector otherSector = new SimpleSector(10, true, false, 20, 15, true, 5, CharacterClass.WARRIOR);
        assertEquals(simpleSector, otherSector);
    }

    @Test
    public void testHashCode() {
        SimpleSector otherSector = new SimpleSector(10, true, false, 20, 15, true, 5, CharacterClass.WARRIOR);
        assertEquals(simpleSector.hashCode(), otherSector.hashCode());
    }

    @After
    public void tearDown() {
        simpleSector = null;
    }
}