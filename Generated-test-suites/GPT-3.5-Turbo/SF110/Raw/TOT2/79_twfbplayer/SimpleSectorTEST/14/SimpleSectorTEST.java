import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SimpleSectorTest {

    @Test
    public void testFree() {
        SimpleSector sector = new SimpleSector(10, true, false, 5, 5, true, 10, CharacterClass.SOLDIER);
        sector.gainControl(CombatantSide.DEFENDER);
        sector.free();
        assertEquals(null, sector.getOccupier());
    }

    @Test
    public void testGainControl() {
        SimpleSector sector = new SimpleSector(10, true, false, 5, 5, true, 10, CharacterClass.SOLDIER);
        sector.gainControl(CombatantSide.ATTACKER);
        assertEquals(CombatantSide.ATTACKER, sector.getOccupier());
    }


    // Additional test cases for other methods would be added here
}