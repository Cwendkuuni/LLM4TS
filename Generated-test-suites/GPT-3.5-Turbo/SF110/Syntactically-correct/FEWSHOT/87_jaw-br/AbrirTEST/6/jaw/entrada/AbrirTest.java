package jaw.entrada;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import jaw.ProcessarEntidades;

public class AbrirTest {
    private Abrir abrir;

    @Before
    public void setUp() {
        ProcessarEntidades form = mock(ProcessarEntidades.class);
        abrir = new Abrir(form, "");
    }

    @Test
    public void testConstructor1() {
        assertEquals("", abrir.xml);
        assertNull(abrir.form);
    }

    @Test
    public void testConstructor2() {
        assertEquals("", abrir.xml);
        assertNull(abrir.form);
    }

    @Test
    public void testAbrirSerializado() {
        abrir.abrirSerializado("test.jaw");

        // Add assertions here
    }
    
    @Test
    public void testAbrirXML() {
        abrir.abrirXML("test.xml");

        // Add assertions here
    }

    @Test
    public void testFiltro2Accept() {
        Abrir.Filtro2 filtro2 = new Abrir().new Filtro2();
        boolean result = filtro2.accept(new File("test.jaw.xml"));
        assertEquals(true, result);

        result = filtro2.accept(new File("test.xml"));
        assertEquals(false, result);
    }

    @Test
    public void testFiltro2GetDescription() {
        Abrir.Filtro2 filtro2 = new Abrir().new Filtro2();
        String result = filtro2.getDescription();
        assertEquals("XML JAW files", result);
    }

    @Test
    public void testFiltroAccept() {
        Abrir.Filtro filtro = new Abrir().new Filtro();
        boolean result = filtro.accept(new File("test.jaw"));
        assertEquals(true, result);

        result = filtro.accept(new File("test.xml"));
        assertEquals(false, result);
    }

    @Test
    public void testFiltroGetDescription() {
        Abrir.Filtro filtro = new Abrir().new Filtro();
        String result = filtro.getDescription();
        assertEquals("JAW files", result);
    }
}