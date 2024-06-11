import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.*;
import de.outstare.fortbattleplayer.model.impl.*;

public class SimpleSectorTest {
    
    private SimpleSector sector;
    
    @Before
    public void setUp() {
        sector = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
    }
    
    @Test
    public void testFree() {
        //testing free method
        sector.free();
        assertEquals(0, sector.getOccupierCount());
    }
    
    @Test
    public void testGainControl() {
        //testing gainControl method
        sector.gainControl(CombatantSide.ATTACKER);
        assertEquals(1, sector.getOccupierCount());
        assertEquals(CombatantSide.ATTACKER, sector.getOccupier());
    }
    
    @Test
    public void testIsOccupied() {
        //testing isOccupied method
        assertFalse(sector.isOccupied());
        sector.gainControl(CombatantSide.DEFENDER);
        assertTrue(sector.isOccupied());
    }
    
    @Test
    public void testGetHeight() {
        //testing getHeight method
        assertEquals(10, sector.getHeight());
    }
    
    // Add more test cases for other methods
    
}