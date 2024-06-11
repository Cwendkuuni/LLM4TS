
import org.junit.Test;
import static org.junit.Assert.*;

public class SalvarTest {

    @Test
    public void testPublicMethods() {
        // Create an instance of Salvar
        Salvar salvar = new Salvar(null, 0);

        // Check the functionality of each public method
        
        // 1. Test the constructor
        assertNotNull(salvar);

        // 2. Test the method accept(File) of Filtro2 class
        Salvar.Filtro2 filtro2 = salvar.new Filtro2();
        File file1 = new File("test.jaw.xml");
        assertTrue(filtro2.accept(file1));
        File file2 = new File("test.java");
        assertFalse(filtro2.accept(file2));

        // 3. Test the method getDescription() of Filtro2 class
        assertEquals("XML JAW files", filtro2.getDescription());

        // 4. Test the method accept(File) of Filtro class
        Salvar.Filtro filtro = salvar.new Filtro();
        File file3 = new File("test.jaw");
        assertTrue(filtro.accept(file3));
        File file4 = new File("test.java");
        assertFalse(filtro.accept(file4));

        // 5. Test the method getDescription() of Filtro class
        assertEquals("JAW files", filtro.getDescription());
    }
}

