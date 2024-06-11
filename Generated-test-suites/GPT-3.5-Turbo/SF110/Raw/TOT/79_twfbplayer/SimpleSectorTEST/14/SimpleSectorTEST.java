import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SimpleSectorTest {
  private SimpleSector simpleSector;

  @Before
  public void setUp() {
    simpleSector = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
  }

  @Test
  public void testFree() {
    simpleSector.gainControl(CombatantSide.DEFENDER);
    simpleSector.free();

    Assert.assertEquals(null, simpleSector.getOccupier());
  }

  @Test
  public void testGainControl() {
    simpleSector.gainControl(CombatantSide.ATTACKER);

    Assert.assertEquals(CombatantSide.ATTACKER, simpleSector.getOccupier());
  }

  @Test
  public void testGetOccupier() {
    simpleSector.gainControl(CombatantSide.DEFENDER);

    Assert.assertEquals(CombatantSide.DEFENDER, simpleSector.getOccupier());
  }

  @Test
  public void testIsOccupied() {
    simpleSector.gainControl(CombatantSide.DEFENDER);

    Assert.assertTrue(simpleSector.isOccupied());
  }

  @Test
  public void testIsNotOccupied() {
    Assert.assertFalse(simpleSector.isOccupied());
  }

  @Test
  public void testGetHeight() {
    Assert.assertEquals(10, simpleSector.getHeight());
  }

  @Test
  public void testIsDefenderSpawn() {
    Assert.assertTrue(simpleSector.isDefenderSpawn());
  }

  @Test
  public void testIsAttackerSpawn() {
    Assert.assertFalse(simpleSector.isAttackerSpawn());
  }

  @Test
  public void testIsFlag() {
    Assert.assertTrue(simpleSector.isFlag());
  }

  @Test
  public void testGetBonus() {
    CharacterClass testClass = CharacterClass.ARCHER;
    SectorBonus expected = new SectorBonus(5, 3, 0);

    Assert.assertEquals(expected, simpleSector.getBonus(testClass));
  }

  @Test
  public void testGetAttackBonus() {
    CharacterClass testClass = CharacterClass.ARCHER;
    int expected = 7;

    Assert.assertEquals(expected, simpleSector.getAttackBonus(testClass));
  }

  @Test
  public void testGetDefenseBonus() {
    CharacterClass testClass = CharacterClass.ARCHER;
    int expected = 5;

    Assert.assertEquals(expected, simpleSector.getDefenseBonus(testClass));
  }

  @Test
  public void testClassBonus() {
    CharacterClass testClass = CharacterClass.ARCHER;
    int expected = 0;

    Assert.assertEquals(expected, simpleSector.classBonus(testClass));
  }

  @Test
  public void testGetAreas() {
    Assert.assertNotNull(simpleSector.getAreas());
  }

  @Test
  public void testAddArea() {
    Area area = new Area();
    simpleSector._addArea(area);

    Assert.assertTrue(simpleSector.getAreas().contains(area));
  }

  @Test
  public void testHashCode() {
    int expected = 379719224;

    Assert.assertEquals(expected, simpleSector.hashCode());
  }

  @Test
  public void testEquals() {
    SimpleSector other = new SimpleSector(10, true, false, 5, 3, true, 2,
        CharacterClass.WARRIOR);

    Assert.assertTrue(simpleSector.equals(other));
  }

  @Test
  public void testAddObserver() {
    SectorObserver observer = new SectorObserverImpl();
    simpleSector.addObserver(observer);

    Assert.assertTrue(simpleSector.getObservers().contains(observer));
  }

  @Test
  public void testRemoveObserver() {
    SectorObserver observer = new SectorObserverImpl();
    simpleSector.addObserver(observer);
    simpleSector.removeObserver(observer);

    Assert.assertFalse(simpleSector.getObservers().contains(observer));
  }

  private class SectorObserverImpl implements SectorObserver {
    @Override
    public void occupierChanged() {
      // implementation not needed for test
    }
  }
}