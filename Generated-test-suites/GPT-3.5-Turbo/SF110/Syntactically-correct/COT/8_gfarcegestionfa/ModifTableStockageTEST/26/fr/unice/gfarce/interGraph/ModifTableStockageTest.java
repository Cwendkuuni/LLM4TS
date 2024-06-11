
import org.junit.Test;
import static org.junit.Assert.*;

public class ModifTableStockageTest {
  
  @Test
  public void testAjouterColonne() {
    TableStockage ts = new TableStockage(2, 2);
    ModifTableStockage mts = new ModifTableStockage(ts);
    
    TableStockage result = mts.ajouterColonne("New Column", String.class);
    
    assertEquals(3, result.getColumnCount());
    assertEquals(2, result.getRowCount());
    assertEquals("New Column", result.getColumnName(2));
  }
  
  @Test
  public void testAjouterLigne() {
    TableStockage ts = new TableStockage(2, 2);
    ModifTableStockage mts = new ModifTableStockage(ts);
    
    TableStockage result = mts.ajouterLigne();
    
    assertEquals(2, result.getColumnCount());
    assertEquals(3, result.getRowCount());
  }
  
  @Test
  public void testSupprimerColonne() {
    TableStockage ts = new TableStockage(2, 3);
    ModifTableStockage mts = new ModifTableStockage(ts);
    
    TableStockage result = mts.supprimerColonne(1);
    
    assertEquals(2, result.getColumnCount());
    assertEquals(2, result.getRowCount());
  }
  
  @Test
  public void testSupprimerLigne() {
    TableStockage ts = new TableStockage(3, 2);
    ModifTableStockage mts = new ModifTableStockage(ts);
    
    TableStockage result = mts.supprimerLigne(1);
    
    assertEquals(2, result.getColumnCount());
    assertEquals(2, result.getRowCount());
  }
  
  @Test
  public void testSeparerColonne() {
    TableStockage ts = new TableStockage(2, 2);
    ModifTableStockage mts = new ModifTableStockage(ts);
    
    TableStockage result = mts.separerColonne(0);
    
    assertEquals(3, result.getColumnCount());
    assertEquals(2, result.getRowCount());
  }
  
  @Test
  public void testFusionnerColonnes() {
    TableStockage ts = new TableStockage(2, 3);
    ModifTableStockage mts = new ModifTableStockage(ts);
    
    TableStockage result = mts.fusionnerColonnes(1);
    
    assertEquals(2, result.getColumnCount());
    assertEquals(2, result.getRowCount());
  }
  
  @Test
  public void testColonneMail() {
    TableStockage ts = new TableStockage(2, 2);
    ModifTableStockage mts = new ModifTableStockage(ts);
    
    TableStockage result = mts.colonneMail();
    
    assertEquals(3, result.getColumnCount());
    assertEquals(2, result.getRowCount());
    assertEquals("Envoyer E-Mail a:", result.getColumnName(2));
  }
  
  @Test
  public void testSelectMail() {
    TableStockage ts = new TableStockage(2, 3);
    ModifTableStockage mts = new ModifTableStockage(ts);
    
    TableStockage result = mts.selectMail();
    
    assertEquals(3, result.getColumnCount());
    assertEquals(2, result.getRowCount());
  }
  
}
