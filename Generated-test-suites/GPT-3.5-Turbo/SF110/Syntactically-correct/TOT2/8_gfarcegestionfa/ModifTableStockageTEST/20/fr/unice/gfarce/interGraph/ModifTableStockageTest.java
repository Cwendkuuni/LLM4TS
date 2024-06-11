package fr.unice.gfarce.interGraph;
import static org.junit.Assert.*;
import org.junit.Test;

public class ModifTableStockageTest {
  
  @Test
  /**
   * Test adding a column to the table
   */
  public void testAjouterColonne() {
    // Test case for adding a column with String type
    ModifTableStockage mts = new ModifTableStockage(new TableStockage(2, 2));
    TableStockage result = mts.ajouterColonne("Test", String.class);
    assertEquals("Test", result.getColumnName(2));
  }
  
  @Test
  /**
   * Test adding a new row to the table
   */
  public void testAjouterLigne() {
    // Test case for adding a new row to the table
    ModifTableStockage mts = new ModifTableStockage(new TableStockage(2, 2));
    TableStockage result = mts.ajouterLigne();
    assertEquals(3, result.getRowCount());
  }
  
  // Add test cases for other methods
  
}