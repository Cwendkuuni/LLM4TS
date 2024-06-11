package fr.unice.gfarce.interGraph;
import static org.junit.Assert.*;

import org.junit.Test;

public class ModifTableStockageTest {

    @Test
    public void testAjouterColonne() {
        // Test adding a column with string type
        ModifTableStockage modifTable = new ModifTableStockage(new TableStockage());
        TableStockage updatedTable = modifTable.ajouterColonne("TestColumn", String.class);
        assertEquals("TestColumn", updatedTable.getColumnName(updatedTable.getColumnCount() - 1));
        
        // Add more test cases for edge cases and exceptions
    }

    @Test
    public void testAjouterLigne() {
        // Test adding a new row
        ModifTableStockage modifTable = new ModifTableStockage(new TableStockage());
        TableStockage updatedTable = modifTable.ajouterLigne();
        assertEquals(2, updatedTable.getRowCount());
        
        // Add more test cases for edge cases and exceptions
    }

    @Test
    public void testSupprimerColonne() {
        // Test removing a column
        ModifTableStockage modifTable = new ModifTableStockage(new TableStockage());
        TableStockage updatedTable = modifTable.supprimerColonne(1);
        assertEquals(0, updatedTable.getColumnCount());
        
        // Add more test cases for edge cases and exceptions
    }

    @Test
    public void testSupprimerLigne() {
        // Test removing a row
        ModifTableStockage modifTable = new ModifTableStockage(new TableStockage());
        TableStockage updatedTable = modifTable.supprimerLigne(0);
        assertEquals(0, updatedTable.getRowCount());
        
        // Add more test cases for edge cases and exceptions
    }

    @Test
    public void testSeparerColonne() {
        // Test separating a column
        ModifTableStockage modifTable = new ModifTableStockage(new TableStockage());
        TableStockage updatedTable = modifTable.separerColonne(0);
        // Add assertions here
        
        // Add more test cases for edge cases and exceptions
    }

    @Test
    public void testFusionnerColonnes() {
        // Test merging two columns
        ModifTableStockage modifTable = new ModifTableStockage(new TableStockage());
        TableStockage updatedTable = modifTable.fusionnerColonnes(0);
        // Add assertions here
        
        // Add more test cases for edge cases and exceptions
    }

    @Test
    public void testColonneMail() {
        // Test adding a "Mail" column
        ModifTableStockage modifTable = new ModifTableStockage(new TableStockage());
        TableStockage updatedTable = modifTable.colonneMail();
        // Add assertions here
        
        // Add more test cases for edge cases and exceptions
    }

    @Test
    public void testSelectMail() {
        // Test selecting rows for sending mails
        ModifTableStockage modifTable = new ModifTableStockage(new TableStockage());
        TableStockage updatedTable = modifTable.selectMail();
        // Add assertions here
        
        // Add more test cases for edge cases and exceptions
    }
}