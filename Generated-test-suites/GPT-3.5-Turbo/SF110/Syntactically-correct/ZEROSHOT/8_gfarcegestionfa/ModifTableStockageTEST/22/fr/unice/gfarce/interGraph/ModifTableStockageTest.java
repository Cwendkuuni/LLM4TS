import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ModifTableStockageTest {
  
  private ModifTableStockage modifTableStockage;
  private TableStockage ts;
  
  @Before
  public void setUp() {
    ts = new TableStockage(3, 3); // create a sample TableStockage object
    modifTableStockage = new ModifTableStockage(ts);
  }
  
  @Test
  public void testAjouterColonne() {
    TableStockage result = modifTableStockage.ajouterColonne("New Column", String.class);
    
    Assert.assertNotNull(result);
    Assert.assertEquals(ts.getColumnCount() + 1, result.getColumnCount());
  }
  
  @Test
  public void testAjouterLigne() {
    TableStockage result = modifTableStockage.ajouterLigne();
    
    Assert.assertNotNull(result);
    Assert.assertEquals(ts.getRowCount() + 1, result.getRowCount());
  }
  
  @Test
  public void testSupprimerColonne() {
    TableStockage result = modifTableStockage.supprimerColonne(0);
    
    Assert.assertNotNull(result);
    Assert.assertEquals(ts.getColumnCount() - 1, result.getColumnCount());
  }
  
  @Test
  public void testSupprimerLigne() {
    TableStockage result = modifTableStockage.supprimerLigne(0);
    
    Assert.assertNotNull(result);
    Assert.assertEquals(ts.getRowCount() - 1, result.getRowCount());
  }
  
  @Test
  public void testSeparerColonne() {
    TableStockage result = modifTableStockage.separerColonne(0);
    
    Assert.assertNotNull(result);
    Assert.assertEquals(ts.getColumnCount() + 1, result.getColumnCount());
  }
  
  @Test
  public void testFusionnerColonnes() {
    TableStockage result = modifTableStockage.fusionnerColonnes(0);
    
    Assert.assertNotNull(result);
    Assert.assertEquals(ts.getColumnCount() - 1, result.getColumnCount());
  }
  
  @Test
  public void testColonneMail() {
    TableStockage result = modifTableStockage.colonneMail();
    
    Assert.assertNotNull(result);
    Assert.assertEquals(ts.getColumnCount() + 1, result.getColumnCount());
  }
  
  @Test
  public void testSelectMail() {
    TableStockage result = modifTableStockage.selectMail();
    
    Assert.assertNotNull(result);
    Assert.assertEquals(ts.getRowCount(), result.getRowCount());
  }
  
}