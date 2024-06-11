
package jaw.entrada;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class AbrirTest {

    private ProcessarEntidades form;
    
    @Before
    public void setUp() {
        form = new ProcessarEntidades();
    }
    
    @Test
    public void testAbrir_XML() {
        Abrir abrir = new Abrir(form, "example.jaw.xml");
        
        // Test opening XML file
        assertEquals("example.jaw.xml", abrir.xml);
        // Test opening XML file with invalid format
        abrir = new Abrir(form, "example.xml");
        assertNull(abrir.xml);
    }
    
    @Test
    public void testAbrir_Serializado() {
        Abrir abrir = new Abrir(form, "example.jaw");
        
        // Test opening serializable file
        assertEquals("example.jaw", abrir.form.getNomeArquivoSalvo());
        
        // Test opening serializable file with invalid format
        abrir = new Abrir(form, "example.text");
        assertNull(abrir.form.getNomeArquivoSalvo());
    }
    
    @Test
    public void testAbrir_InvalidType() {
        Abrir abrir = new Abrir(form, 30);
        
        assertNull(abrir.xml);
        assertNull(abrir.form.getNomeArquivoSalvo());
        
        abrir = new Abrir(form, -10);
        
        assertNull(abrir.xml);
        assertNull(abrir.form.getNomeArquivoSalvo());
    }
}
