package fr.unice.gfarce.interGraph;
import static org.junit.Assert.*;
import org.junit.Test;

public class ModifTableStockageTest {

    @Test
    public void testAjouterColonne() {
        // Test adding a new column with String type
        ModifTableStockage modifTable = new ModifTableStockage(new TableStockage(3, 3));
        TableStockage result = modifTable.ajouterColonne("New Column", String.class);
        assertEquals("New Column", result.getColumnName(3));
        assertEquals(String.class, result.getColumnClass(3));
        
        // Test adding a new column with Integer type
        result = modifTable.ajouterColonne("Numeric Column", Integer.class);
        assertEquals("Numeric Column", result.getColumnName(4));
        assertEquals(Integer.class, result.getColumnClass(4));
    }

    @Test
    public void testAjouterLigne() {
        // Test adding a new row to the table
        ModifTableStockage modifTable = new ModifTableStockage(new TableStockage(3, 3));
        TableStockage result = modifTable.ajouterLigne();
        assertEquals(4, result.getRowCount());
        
        // Test adding a new row to an empty table
        modifTable = new ModifTableStockage(new TableStockage(0, 0));
        result = modifTable.ajouterLigne();
        assertEquals(1, result.getRowCount());
    }

    @Test
    public void testSupprimerColonne() {
        // Test removing a column
        ModifTableStockage modifTable = new ModifTableStockage(new TableStockage(3, 3));
        TableStockage result = modifTable.supprimerColonne(1);
        assertEquals(2, result.getColumnCount());
        
        // Test removing a column from an empty table
        modifTable = new ModifTableStockage(new TableStockage(0, 0));
        result = modifTable.supprimerColonne(0);
        assertEquals(0, result.getColumnCount());
    }

    @Test
    public void testSupprimerLigne() {
        // Test removing a row
        ModifTableStockage modifTable = new ModifTableStockage(new TableStockage(3, 3));
        TableStockage result = modifTable.supprimerLigne(1);
        assertEquals(2, result.getRowCount());
        
        // Test removing a row from an empty table
        modifTable = new ModifTableStockage(new TableStockage(0, 0));
        result = modifTable.supprimerLigne(0);
        assertEquals(0, result.getRowCount());
    }

    @Test
    public void testSeparerColonne() {
        // Test separating a column with a single value into two columns
        ModifTableStockage modifTable = new ModifTableStockage(new TableStockage(3, 3));
        TableStockage result = modifTable.separerColonne(1);
        assertEquals(4, result.getColumnCount());
        
        // Test separating a column with multiple values into two columns
        result = modifTable.separerColonne(2);
        assertEquals(5, result.getColumnCount());
    }

    @Test
    public void testFusionnerColonnes() {
        // Test merging two columns into one
        ModifTableStockage modifTable = new ModifTableStockage(new TableStockage(3, 3));
        TableStockage result = modifTable.fusionnerColonnes(1);
        assertEquals(2, result.getColumnCount());
        
        // Test merging two columns of different types
        result = modifTable.fusionnerColonnes(2);
        assertEquals(2, result.getColumnCount());
    }

    @Test
    public void testColonneMail() {
        // Test adding a new column for email with Boolean type
        ModifTableStockage modifTable = new ModifTableStockage(new TableStockage(3, 3));
        TableStockage result = modifTable.colonneMail();
        assertEquals(4, result.getColumnCount());
        assertEquals(Boolean.class, result.getColumnClass(3));
    }

    @Test
    public void testSelectMail() {
        // Test selecting rows for sending email
        ModifTableStockage modifTable = new ModifTableStockage(new TableStockage(3, 3));
        TableStockage result = modifTable.selectMail();
        assertEquals(2, result.getRowCount());
        
        // Test selecting rows when email column does not exist
        modifTable = new ModifTableStockage(new TableStockage(3, 2));
        result = modifTable.selectMail();
        assertEquals(3, result.getRowCount());
    }
}