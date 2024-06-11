package jaw.entrada;

import static org.junit.Assert.*;
import org.junit.Test;

public class AbrirTest {

    @Test
    public void testConstructor1() {
        ProcessarEntidades form = new ProcessarEntidades();
        Abrir abrir = new Abrir(form, "sample.jaw.xml");
        assertNotNull(abrir);
    }

    @Test
    public void testConstructor2() {
        ProcessarEntidades form = new ProcessarEntidades();
        Abrir abrir = new Abrir(form, 10);
        assertNotNull(abrir);
    }

    @Test
    public void testAbrirSerializado() {
        ProcessarEntidades form = new ProcessarEntidades();
        Abrir abrir = new Abrir(form, "sample.jaw");
        assertNotNull(abrir);
    }

    @Test
    public void testAbrirXML() {
        ProcessarEntidades form = new ProcessarEntidades();
        Abrir abrir = new Abrir(form, 20);
        assertNotNull(abrir);
    }

    @Test
    public void testFiltro2Accept() {
        Abrir.Filtro2 filtro2 = new Abrir.Filtro2();
        assertTrue(filtro2.accept(new File("sample.jaw.xml")));
        assertFalse(filtro2.accept(new File("sample.jpg")));
    }

    @Test
    public void testFiltroDescription() {
        Abrir.Filtro2 filtro2 = new Abrir.Filtro2();
        assertEquals("XML JAW files", filtro2.getDescription());
    }

    @Test
    public void testFiltroAccept() {
        Abrir.Filtro filtro = new Abrir.Filtro();
        assertTrue(filtro.accept(new File("sample.jaw")));
        assertFalse(filtro.accept(new File("sample.xml")));
    }

    @Test
    public void testFiltroDescription() {
        Abrir.Filtro filtro = new Abrir.Filtro();
        assertEquals("JAW files", filtro.getDescription());
    }

}