package jaw.entrada;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class AbrirTest {
    @Test
    public void testAbrirConstructor() {
        ProcessarEntidades form = new ProcessarEntidades();
        String arquivo = "testFile.jaw.xml";
        Abrir abrir = new Abrir(form, arquivo);
        assertEquals("", abrir.xml);
        assertEquals(form, abrir.form);
    }
    
    @Test
    public void testAbrirConstructorWithType() {
        ProcessarEntidades form = new ProcessarEntidades();
        int tipoAbrir = 10;
        Abrir abrir = new Abrir(form, tipoAbrir);
        assertEquals("", abrir.xml);
        assertEquals(form, abrir.form);
    }
    
    @Test
    public void testAbrirSerializado() {
        ProcessarEntidades form = new ProcessarEntidades();
        Abrir abrir = new Abrir(form, "testFile.jaw");
        
        assertNull(abrir.form.getEntidades());
        assertEquals("Arquivo jaw corrompido ou incompatÃ­vel com aplicaÃ§Ã£o!", Main.janelaPrincipal.log);
    }
    
    @Test
    public void testAbrirXML() {
        ProcessarEntidades form = new ProcessarEntidades();
        Abrir abrir = new Abrir(form, "testFile.jaw.xml");
        
        assertNull(abrir.form.getEntidades());
        assertEquals("Arquivo xml corrompido ou incompatÃ­vel com aplicaÃ§Ã£o!", Main.janelaPrincipal.log);
    }
    
    @Test
    public void testFiltro2Accept() {
        Abrir.Filtro2 filtro = new Abrir.Filtro2();
        File file = new File("testFile.jaw.xml");
        assertTrue(filtro.accept(file));
    }
    
    @Test
    public void testFiltro2AcceptFalse() {
        Abrir.Filtro2 filtro = new Abrir.Filtro2();
        File file = new File("testFile.jaw");
        assertFalse(filtro.accept(file));
    }
    
    @Test
    public void testFiltroAccept() {
        Abrir.Filtro filtro = new Abrir.Filtro();
        File file = new File("testFile.jaw");
        assertTrue(filtro.accept(file));
    }
    
    @Test
    public void testFiltroAcceptFalse() {
        Abrir.Filtro filtro = new Abrir.Filtro();
        File file = new File("testFile.jaw.xml");
        assertFalse(filtro.accept(file));
    }
}