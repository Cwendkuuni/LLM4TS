package jaw.entrada;

import jaw.gui.*;
import jaw.*;
import javax.swing.filechooser.*;
import java.awt.*;
import javax.swing.*;
import java.io.*;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AbrirTest {

    private ProcessarEntidades form;
    private Abrir abrirXML;
    private Abrir abrirSerializado;

    @Before
    public void setUp() {
        form = new ProcessarEntidades();
        abrirXML = new Abrir(form, Abrir.ABRIR_XML);
        abrirSerializado = new Abrir(form, Abrir.ABRIR);
    }

    @Test
    public void testAbrirXML() {
        assertEquals("", abrirXML.xml);
        assertNotNull(abrirXML.form);
        // TODO: Implement additional assertions
    }

    @Test
    public void testAbrirSerializado() {
        assertEquals("", abrirSerializado.xml);
        assertNotNull(abrirSerializado.form);
        // TODO: Implement additional assertions
    }

    @Test
    public void testFiltro2Accept() {
        File file = new File("test.jaw.xml");
        Abrir.Filtro2 filtro2 = new Abrir.Filtro2();
        assertTrue(filtro2.accept(file));
    }

    @Test
    public void testFiltro2GetDescription() {
        Abrir.Filtro2 filtro2 = new Abrir.Filtro2();
        assertEquals("XML JAW files", filtro2.getDescription());
    }

    @Test
    public void testFiltroAccept() {
        File file = new File("test.jaw");
        Abrir.Filtro filtro = new Abrir.Filtro();
        assertTrue(filtro.accept(file));
    }

    @Test
    public void testFiltroGetDescription() {
        Abrir.Filtro filtro = new Abrir.Filtro();
        assertEquals("JAW files", filtro.getDescription());
    }

}