import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleSectorTest {

    // Test case for the constructor method
    @Test
    public void testConstructor() {
        SimpleSector sector = new SimpleSector(5, true, false, 10, 20, true, 5, CharacterClass.WARRIOR);

        assertEquals(5, sector.getHeight());
        assertTrue(sector.isDefenderSpawn());
        assertFalse(sector.isAttackerSpawn());
        assertTrue(sector.isFlag());
        assertEquals(CharacterClass.WARRIOR, sector.getBonus().getCharacterClass());
    }

    // Test case for the free method
    @Test
    public void testFree() {
        SimpleSector sector = new SimpleSector(5, true, false, 10, 20, true, 5, CharacterClass.WARRIOR);
        
        sector.free();
        assertEquals(0, sector.getOccupierCount());
        assertNull(sector.getOccupier());

        // Test case for more combatants left this sector than enter it
        sector.gainControl(CombatantSide.ATTACKER);
        sector.free();
        assertEquals(0, sector.getOccupierCount());
        assertNull(sector.getOccupier());
    }

    // Test case for the occupancy methods
    @Test
    public void testOccupency() {
        SimpleSector sector = new SimpleSector(5, true, false, 10, 20, true, 5, CharacterClass.WARRIOR);
        
        assertFalse(sector.isOccupied());
        assertNull(sector.getOccupier());

        sector.gainControl(CombatantSide.ATTACKER);
        assertTrue(sector.isOccupied());
        assertEquals(CombatantSide.ATTACKER, sector.getOccupier());
    }

    // Test case for the getBonus method
    @Test
    public void testGetBonus() {
        SimpleSector sector = new SimpleSector(5, true, false, 10, 20, true, 5, CharacterClass.WARRIOR);

        SectorBonus bonus = sector.getBonus(CharacterClass.MAGE);
        assertEquals(15, bonus.getAttackBonus());
        assertEquals(25, bonus.getDefenseBonus());
        assertEquals(0, bonus.getDamageBonus());

        bonus = sector.getBonus(CharacterClass.WARRIOR);
        assertEquals(20, bonus.getAttackBonus());
        assertEquals(30, bonus.getDefenseBonus());
        assertEquals(0, bonus.getDamageBonus());
    }

    // Test case for the getAreas method
    @Test
    public void testGetAreas() {
        SimpleSector sector = new SimpleSector(5, true, false, 10, 20, true, 5, CharacterClass.WARRIOR);

        assertTrue(sector.getAreas().isEmpty());

        Area area1 = new Area();
        sector._addArea(area1);
        assertEquals(1, sector.getAreas().size());
        assertTrue(sector.getAreas().contains(area1));

        Area area2 = new Area();
        sector._addArea(area2);
        assertEquals(2, sector.getAreas().size());
        assertTrue(sector.getAreas().contains(area2));
    }

    // Test case for the equals method
    @Test
    public void testEquals() {
        SimpleSector sector1 = new SimpleSector(5, true, false, 10, 20, true, 5, CharacterClass.WARRIOR);
        SimpleSector sector2 = new SimpleSector(5, true, false, 10, 20, true, 5, CharacterClass.WARRIOR);
        SimpleSector sector3 = new SimpleSector(10, true, false, 10, 20, true, 5, CharacterClass.WARRIOR);

        assertTrue(sector1.equals(sector2));
        assertFalse(sector1.equals(sector3));
    }

    // Test case for the addObserver and removeObserver methods
    @Test
    public void testObserver() {
        SimpleSector sector = new SimpleSector(5, true, false, 10, 20, true, 5, CharacterClass.WARRIOR);

        assertTrue(sector.getObservers().isEmpty());

        SectorObserver observer1 = new SectorObserver();
        sector.addObserver(observer1);
        assertEquals(1, sector.getObservers().size());
        assertTrue(sector.getObservers().contains(observer1));

        SectorObserver observer2 = new SectorObserver();
        sector.addObserver(observer2);
        assertEquals(2, sector.getObservers().size());
        assertTrue(sector.getObservers().contains(observer2));

        sector.removeObserver(observer1);
        assertEquals(1, sector.getObservers().size());
        assertFalse(sector.getObservers().contains(observer1));
    }

    // Test case for the private method getBonusFromWeapons
    @Test
    public void testGetBonusFromWeapons() {
        SimpleSector sector = new SimpleSector(5, true, false, 10, 20, true, 5, CharacterClass.WARRIOR);
        
        // Test case when no area is occupied
        int bonus = sector.getBonusFromWeapons(SectorBonus.BonusType.DAMAGE);
        assertEquals(0, bonus);

        // Test case when an area is occupied with a weapon that has a bonus
        Combatant combatant = new Combatant();
        Weapon weapon = new Weapon();
        weapon.setSectorBonus(SectorBonus.BonusType.DAMAGE, 15);
        combatant.setWeapon(weapon);
        Area area = new Area();
        area.setOccupier(combatant);
        sector._addArea(area);

        bonus = sector.getBonusFromWeapons(SectorBonus.BonusType.DAMAGE);
        assertEquals(15, bonus);
    }

    // Test case for the protected method classBonus
    @Test
    public void testClassBonus() {
        SimpleSector sector = new SimpleSector(5, true, false, 10, 20, true, 5, CharacterClass.WARRIOR);

        int bonus = sector.classBonus(CharacterClass.MAGE);
        assertEquals(0, bonus);

        bonus = sector.classBonus(CharacterClass.WARRIOR);
        assertEquals(5, bonus);
    }

    // Test case for the protected methods getAttackBonus and getDefenseBonus
    @Test
    public void testAttackDefenseBonus() {
        SimpleSector sector = new SimpleSector(5, true, false, 10, 20, true, 5, CharacterClass.WARRIOR);
        
        // Test case when no weapon bonus is available
        int attackBonus = sector.getAttackBonus(CharacterClass.MAGE);
        assertEquals(15, attackBonus);

        int defenseBonus = sector.getDefenseBonus(CharacterClass.MAGE);
        assertEquals(25, defenseBonus);

        attackBonus = sector.getAttackBonus(CharacterClass.WARRIOR);
        assertEquals(20, attackBonus);

        defenseBonus = sector.getDefenseBonus(CharacterClass.WARRIOR);
        assertEquals(30, defenseBonus);

        // Test case when weapon bonus is available
        Combatant combatant = new Combatant();
        Weapon weapon = new Weapon();
        weapon.setSectorBonus(SectorBonus.BonusType.ATTACK, 10);
        weapon.setSectorBonus(SectorBonus.BonusType.DEFENSE, 20);
        combatant.setWeapon(weapon);
        Area area = new Area();
        area.setOccupier(combatant);
        sector._addArea(area);

        attackBonus = sector.getAttackBonus(CharacterClass.MAGE);
        assertEquals(25, attackBonus);

        defenseBonus = sector.getDefenseBonus(CharacterClass.MAGE);
        assertEquals(45, defenseBonus);

        attackBonus = sector.getAttackBonus(CharacterClass.WARRIOR);
        assertEquals(30, attackBonus);

        defenseBonus = sector.getDefenseBonus(CharacterClass.WARRIOR);
        assertEquals(50, defenseBonus);
    }

}