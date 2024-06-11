import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class SimpleSectorTest {
    
    private SimpleSector sector;
    private Area area1;
    private Area area2;
    private Combatant combatant1;
    private Combatant combatant2;
    
    @Before
    public void setUp() {
        sector = new SimpleSector(10, true, false, 5, 10, true, 2, CharacterClass.WARRIOR);
        
        area1 = new Area();
        combatant1 = new Combatant();
        combatant1.setWeapon(new Weapon("Sword", 3, 2));
        area1.setOccupier(combatant1);
        
        area2 = new Area();
        combatant2 = new Combatant();
        combatant2.setWeapon(new Weapon("Bow", 2, 1));
        area2.setOccupier(combatant2);
        
        sector._addArea(area1);
        sector._addArea(area2);
    }
    
    @Test
    public void testFree() {
        sector.gainControl(CombatantSide.DEFENDER);
        sector.free();
        assertTrue(sector.getOccupier() == null);
    }
    
    @Test
    public void testGainControl() {
        sector.gainControl(CombatantSide.ATTACKER);
        assertTrue(sector.getOccupier() == CombatantSide.ATTACKER);
    }
    
    @Test
    public void testGetOccupier() {
        sector.gainControl(CombatantSide.DEFENDER);
        assertTrue(sector.getOccupier() == CombatantSide.DEFENDER);
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
        SectorBonus expectedBonus = new SectorBonus(7, 12, 2);
        assertEquals(expectedBonus, sector.getBonus(CharacterClass.WARRIOR));
    }
    
    @Test
    public void testGetAreas() {
        Set<Area> expectedAreas = new HashSet<Area>();
        expectedAreas.add(area1);
        expectedAreas.add(area2);
        assertEquals(expectedAreas, sector.getAreas());
    }
    
    @Test
    public void testAddObserver() {
        SectorObserver observer = new MockSectorObserver();
        sector.addObserver(observer);
        assertTrue(sector.getObservers().contains(observer));
    }
    
    @Test
    public void testRemoveObserver() {
        SectorObserver observer = new MockSectorObserver();
        sector.addObserver(observer);
        sector.removeObserver(observer);
        assertFalse(sector.getObservers().contains(observer));
    }
    
    @Test
    public void testHashCode() {
        int expectedHashCode = sector.getAreas().hashCode() + sector.getAttackBonus(CharacterClass.WARRIOR) + sector.isAttackerSpawn() ? 1231 : 1237 + sector.getClassBonus() + sector.getClassType().hashCode() + sector.getDefenseBonus(CharacterClass.WARRIOR) + sector.isDefenderSpawn() ? 1231 : 1237 + sector.isFlag() ? 1231 : 1237 + sector.getHeight();
        assertEquals(expectedHashCode, sector.hashCode());
    }
    
    @Test
    public void testEquals() {
        SimpleSector sameSector = new SimpleSector(10, true, false, 5, 10, true, 2, CharacterClass.WARRIOR);
        assertTrue(sector.equals(sameSector));
        
        SimpleSector differentSector = new SimpleSector(10, false, true, 10, 20, false, 3, CharacterClass.MAGE);
        assertFalse(sector.equals(differentSector));
    }
    
    class MockSectorObserver implements SectorObserver {
        @Override
        public void occupierChanged() {
            // Do nothing
        }
    }
}