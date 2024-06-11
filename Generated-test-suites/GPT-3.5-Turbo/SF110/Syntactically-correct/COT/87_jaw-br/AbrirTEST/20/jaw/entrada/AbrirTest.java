
import static org.junit.Assert.*;
import org.junit.Test;

public class AbrirTest {

    @Test
    public void testAbrirConstructorWithXMLFile() {
        ProcessarEntidades form = new ProcessarEntidades();
        String arquivo = "testFile.jaw.xml";
        Abrir abrir = new Abrir(form, arquivo);
        assertEquals("", abrir.xml);
        assertEquals(form, abrir.form);     
    }
    
    @Test
    public void testAbrirConstructorWithSerializadoFile() {
        ProcessarEntidades form = new ProcessarEntidades();
        String arquivo = "testFile.jaw";
        Abrir abrir = new Abrir(form, arquivo);
        assertEquals("", abrir.xml);
        assertEquals(form, abrir.form);     
    }
    
    @Test
    public void testAbrirConstructorWithInvalidFileExtension() {
        ProcessarEntidades form = new ProcessarEntidades();
        String arquivo = "testFile.txt";
        Abrir abrir = new Abrir(form, arquivo);
        assertEquals("", abrir.xml);
        assertEquals(form, abrir.form);     
    }
    
    @Test
    public void testAbrirConstructorWithABRIR_XMLType() {
        ProcessarEntidades form = new ProcessarEntidades();
        int tipoAbrir = 10;
        Abrir abrir = new Abrir(form, tipoAbrir);
        assertEquals("", abrir.xml);
        assertEquals(form, abrir.form);     
    }
    
    @Test
    public void testAbrirConstructorWithABRIRType() {
        ProcessarEntidades form = new ProcessarEntidades();
        int tipoAbrir = 20;
        Abrir abrir = new Abrir(form, tipoAbrir);
        assertEquals("", abrir.xml);
        assertEquals(form, abrir.form);     
    }
    
}
