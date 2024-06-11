package fr.unice.gfarce.interGraph;

import javax.swing.*;
import java.awt.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ModifTableStockageTest {
    
    ModifTableStockage testObj;
    TableStockage ts;
    int initialNbColonnes;
    int initialNbLignes;
    
    @Before
    public void setup() {
        ts = new TableStockage();
        testObj = new ModifTableStockage(ts);
        initialNbColonnes = ts.getColumnCount();
        initialNbLignes = ts.getRowCount();
    }
    
    @Test
    public void testAjouterColonne() {
        int expectedNbColonnes = initialNbColonnes + 1;
        int expectedNbLignes = initialNbLignes + 1;
        TableStockage result = testObj.ajouterColonne("colonne", String.class);
        assertEquals(expectedNbColonnes, result.getColumnCount());
        assertEquals(expectedNbLignes, result.getRowCount());
        assertEquals("colonne", result.getColumnName(initialNbColonnes));
        assertEquals(String.class, result.getColumnClass(initialNbColonnes));
    }
    
    @Test
    public void testAjouterLigne() {
        int expectedNbColonnes = initialNbColonnes;
        int expectedNbLignes = initialNbLignes + 1;
        TableStockage result = testObj.ajouterLigne();
        assertEquals(expectedNbColonnes, result.getColumnCount());
        assertEquals(expectedNbLignes, result.getRowCount());
    }
    
    @Test
    public void testSupprimerColonne() {
        int indice = 0;
        int expectedNbColonnes = initialNbColonnes - 1;
        int expectedNbLignes = initialNbLignes;
        TableStockage result = testObj.supprimerColonne(indice);
        assertEquals(expectedNbColonnes, result.getColumnCount());
        assertEquals(expectedNbLignes, result.getRowCount());
    }
    
    @Test
    public void testSupprimerLigne() {
        int indice = 0;
        int expectedNbColonnes = initialNbColonnes;
        int expectedNbLignes = initialNbLignes - 1;
        TableStockage result = testObj.supprimerLigne(indice);
        assertEquals(expectedNbColonnes, result.getColumnCount());
        assertEquals(expectedNbLignes, result.getRowCount());
    }
    
    @Test
    public void testSeparerColonne() {
        int indice = 0;
        int expectedNbColonnes = initialNbColonnes + 1;
        int expectedNbLignes = initialNbLignes;
        TableStockage result = testObj.separerColonne(indice);
        assertEquals(expectedNbColonnes, result.getColumnCount());
        assertEquals(expectedNbLignes, result.getRowCount());
    }
    
    @Test
    public void testSeparerColonneNonSeparable() {
        int indice = 0;
        ts.setColumnClass(indice, Double.class);
        TableStockage result = testObj.separerColonne(indice);
        assertEquals(ts, result);
    }
    
    @Test
    public void testFusionnerColonnes() {
        int indice = 0;
        int expectedNbColonnes = initialNbColonnes - 1;
        int expectedNbLignes = initialNbLignes;
        TableStockage result = testObj.fusionnerColonnes(indice);
        assertEquals(expectedNbColonnes, result.getColumnCount());
        assertEquals(expectedNbLignes, result.getRowCount());
    }
    
    @Test
    public void testFusionnerColonnesUneSeuleColonne() {
        ts.setColumnCount(1);
        TableStockage result = testObj.fusionnerColonnes(0);
        assertEquals(ts, result);
    }
    
    @Test
    public void testColonneMail() {
        int expectedNbColonnes = initialNbColonnes + 1;
        int expectedNbLignes = initialNbLignes + 1;
        TableStockage result = testObj.colonneMail();
        assertEquals(expectedNbColonnes, result.getColumnCount());
        assertEquals(expectedNbLignes, result.getRowCount());
        assertEquals("Envoyer E-Mail a:", result.getColumnName(initialNbColonnes));
        assertEquals(Boolean.class, result.getColumnClass(initialNbColonnes));
    }
    
    @Test
    public void testSelectMail() {
        int expectedNbColonnes = initialNbColonnes;
        int expectedNbLignes = initialNbLignes;
        TableStockage result = testObj.selectMail();
        assertEquals(expectedNbColonnes, result.getColumnCount());
        assertEquals(expectedNbLignes, result.getRowCount());
    }
}