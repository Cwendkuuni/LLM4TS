import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class AbrirTest {
    
    private Abrir abrir;
    private ProcessarEntidades form;
    
    @Before
    public void setUp() {
        form = new ProcessarEntidades();
        abrir = new Abrir(form, "testFile.jaw.xml");
    }
    
    @Test
    public void testAbrirConstructor() {
        assertNotNull(abrir);
    }
    
    @Test
    public void testAbrirConstructorWithType() {
        Abrir abrir2 = new Abrir(form, Abrir.ABRIR_XML);
        assertNotNull(abrir2);
    }
    
    @Test
    public void testAbirSerializado() {
        abrir.abrirSerializado("testFile.jaw");
        assertEquals("testFile.jaw", form.getNomeArquivoSalvo());
        assertNotNull(form.getEntidades());
    }
    
    @Test
    public void testAbrirXML() {
        abrir.abrirXML("testFile.jaw.xml");
        assertEquals("testFile.jaw.xml", form.getNomeArquivoSalvo());
        assertNotNull(form.getEntidades());
    }
}