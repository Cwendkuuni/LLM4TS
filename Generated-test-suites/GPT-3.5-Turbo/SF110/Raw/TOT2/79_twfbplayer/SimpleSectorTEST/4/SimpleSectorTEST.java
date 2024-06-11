import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import de.outstare.fortbattleplayer.model.impl.*;
import de.outstare.fortbattleplayer.model.*;

public class SimpleSectorTest {
    private SimpleSector sector;
    
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
        assertEquals(1, sector.getOccupierCount());
        assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
    }
    
    @Test
    public void testGetBonus() {
        SectorBonus bonus = sector.getBonus(CharacterClass.WARRIOR);
        assertNotNull(bonus);
        assertEquals(7, bonus.getAttackBonus());
        assertEquals(5, bonus.getDefenseBonus());
    }
    
    @Test
    public void testGetDamageBonus() {
        assertEquals(0, sector.getDamageBonus());
    }
    
    @Test
    public void testGetAreas() {
        assertTrue(sector.getAreas().isEmpty());
        Area area = new Area();
        sector._addArea(area);
        assertEquals(1, sector.getAreas().size());
        assertTrue(sector.getAreas().contains(area));
    }
    
    @Test
    public void testHashCode() {
        assertEquals(sector.hashCode(), sector.hashCode());
    }
    
    @Test
    public void testEquals() {
        SimpleSector other = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
        assertTrue(sector.equals(other));
    }
}