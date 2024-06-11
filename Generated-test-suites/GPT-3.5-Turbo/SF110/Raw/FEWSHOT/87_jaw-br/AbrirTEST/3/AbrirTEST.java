package jaw.entrada;

import static org.junit.Assert.*;

import org.junit.Test;

public class AbrirTest {
    
    @Test
    public void testConstructorWithArquivo() {
        ProcessarEntidades form = new ProcessarEntidades();
        Abrir abrir = new Abrir(form, "test.jaw.xml");
        
        assertEquals("test.jaw.xml", abrir.xml);
        assertEquals(form, abrir.form);
    }
    
    @Test
    public void testConstructorWithTipoAbrir() {
        ProcessarEntidades form = new ProcessarEntidades();
        Abrir abrir = new Abrir(form, 10);
        
        assertEquals("", abrir.xml);
        assertEquals(form, abrir.form);
    }
    
    @Test
    public void testAbrirSerializadoWithValidFile() {
        ProcessarEntidades form = new ProcessarEntidades();
        Abrir abrir = new Abrir(form, "test.jaw");
        
        assertEquals("test.jaw", form.getNomeArquivoSalvo());
        assertNotNull(form.getEntidades());
    }
    
    @Test
    public void testAbrirSerializadoWithInvalidFile() {
        ProcessarEntidades form = new ProcessarEntidades();
        Abrir abrir = new Abrir(form, "invalid.jaw");

        assertNull(form.getNomeArquivoSalvo());
        assertNull(form.getEntidades());
    }
    
    @Test
    public void testAbrirXMLWithValidFile() {
        ProcessarEntidades form = new ProcessarEntidades();
        Abrir abrir = new Abrir(form, "test.jaw.xml");
        
        assertEquals("test.jaw.xml", form.getNomeArquivoSalvo());
        assertNotNull(form.getEntidades());
    }
    
    @Test
    public void testAbrirXMLWithInvalidFile() {
        ProcessarEntidades form = new ProcessarEntidades();
        Abrir abrir = new Abrir(form, "invalid.xml");
        
        assertNull(form.getNomeArquivoSalvo());
        assertNull(form.getEntidades());
    }
}