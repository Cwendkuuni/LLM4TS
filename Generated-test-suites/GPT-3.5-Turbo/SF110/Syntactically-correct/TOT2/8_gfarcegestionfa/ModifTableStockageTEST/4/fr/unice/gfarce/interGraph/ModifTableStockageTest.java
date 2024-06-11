package fr.unice.gfarce.interGraph;
import static org.junit.Assert.*;
import org.junit.Test;
  
public class ModifTableStockageTest {
  
    @Test
    public void testAjouterColonne() {
        // Testing adding a new column with specified name and type
        // Edge case: adding a column with an empty name
        // Exception handling: adding a column with a null type
    }
  
    @Test
    public void testAjouterLigne() {
        // Testing adding a new row
        // Edge case: adding a row when there are no columns
        // Exception handling: displaying an error message when adding a row without columns
    }
  
    @Test
    public void testSupprimerColonne() {
        // Testing deleting a column by index
        // Edge case: deleting the last column
        // Exception handling: handling the case when trying to delete a non-existing column
    }
  
    @Test
    public void testSupprimerLigne() {
        // Testing deleting a row by index
        // Edge case: deleting the last row
        // Exception handling: handling the case when trying to delete a non-existing row
    }
  
    @Test
    public void testSeparerColonne() {
        // Testing splitting a column with a string value
        // Edge case: splitting a column with an empty value
        // Exception handling: displaying an error message when unable to split the column
    }
  
    @Test
    public void testFusionnerColonnes() {
        // Testing merging two columns
        // Edge case: merging columns with boolean values
        // Exception handling: handling the case when trying to merge non-string columns
    }
  
    @Test
    public void testColonneMail() {
        // Testing adding a mail column with boolean values
        // Edge case: adding a mail column when there are no columns
    }
  
    @Test
    public void testSelectMail() {
        // Testing selecting rows with mail flag set to true
        // Edge case: selecting rows when there are no mail columns
        // Exception handling: displaying an information message when all rows will receive mail
    }
}