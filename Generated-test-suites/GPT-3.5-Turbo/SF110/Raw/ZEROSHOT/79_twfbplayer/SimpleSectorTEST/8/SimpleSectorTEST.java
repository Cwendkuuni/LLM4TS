import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.HashSet;
import java.util.Set;
import de.outstare.fortbattleplayer.model.*;
import de.outstare.fortbattleplayer.model.impl.SimpleSector;

public class SimpleSectorTest {

    private SimpleSector sector;

    @Before
    public void setUp() {
        sector = new SimpleSector(10, true, false, 5, 10, true, 15, CharacterClass.WARRIOR);
    }

    @Test
    public void testFree() {
        sector.gainControl(CombatantSide.ATTACKER);
        sector.free();
        assertNull(sector.getOccupier());
    }

    @Test
    public void testGainControl() {
        sector.gainControl(CombatantSide.DEFENDER);
        assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
    }

    @Test
    public void testGetOccupier() {
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
        assertEquals(20, bonus.getAttackBonus());
        assertEquals(25, bonus.getDefenseBonus());
        assertEquals(0, bonus.getDamageBonus());
    }

    @Test
    public void testGetAreas() {
        Set<Area> areas = new HashSet<>();
        areas.add(new Area());
        areas.add(new Area());
        sector._addArea(new Area());
        sector._addArea(new Area());
        assertEquals(areas, sector.getAreas());
    }

    @Test
    public void testHashCode() {
        SimpleSector other = new SimpleSector(10, true, false, 5, 10, true, 15, CharacterClass.WARRIOR);
        assertEquals(sector.hashCode(), other.hashCode());
    }

    @Test
    public void testEquals() {
        SimpleSector other = new SimpleSector(10, true, false, 5, 10, true, 15, CharacterClass.WARRIOR);
        assertTrue(sector.equals(other));
    }  
    
    @Test
    public void testAddObserver() {
        SectorObserver observer = new SectorObserver(){
            @Override
            public void occupierChanged() {
                // custom implementation
            }
        };
        sector.addObserver(observer);
        assertTrue(sector.getObservers().contains(observer));
    }
  
    @Test
    public void testRemoveObserver() {
        SectorObserver observer = new SectorObserver(){
            @Override
            public void occupierChanged() {
                // custom implementation
            }
        };
        sector.addObserver(observer);
        sector.removeObserver(observer);
        assertFalse(sector.getObservers().contains(observer));
    }
}