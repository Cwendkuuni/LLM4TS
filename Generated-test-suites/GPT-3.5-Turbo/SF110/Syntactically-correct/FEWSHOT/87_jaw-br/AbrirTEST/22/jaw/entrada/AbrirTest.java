package jaw.entrada;

import static jaw.entrada.Abrir.*;
import static org.junit.Assert.*;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import org.junit.Before;
import org.junit.Test;

public class AbrirTest {

    private ProcessarEntidades form;
    private String filePath;

    @Before
    public void setup() {
        form = new ProcessarEntidades();
        filePath = "test.jaw.xml"; // replace with appropriate file path
    }

    @Test
    public void testConstructor1() {
        Abrir abrir = new Abrir(form, filePath);
        assertEquals("", abrir.xml);
        assertEquals(form, abrir.form);
        // add additional assertions if necessary
    }

    @Test
    public void testConstructor2() {
        Abrir abrir = new Abrir(form, ABRIR_XML);
        assertEquals("", abrir.xml);
        assertEquals(form, abrir.form);
        // add additional assertions if necessary
    }

    @Test
    public void testConstructor3() {
        Abrir abrir = new Abrir(form, ABRIR);
        assertEquals("", abrir.xml);
        assertEquals(form, abrir.form);
        // add additional assertions if necessary
    }

    @Test
    public void testAbrirSerializado() {
        Abrir abrir = new Abrir(form, filePath);
        abrir.abrirSerializado(filePath);
        // add assertions to test the behavior of abrirSerializado method
    }

    @Test
    public void testAbrirXML() {
        Abrir abrir = new Abrir(form, filePath);
        abrir.abrirXML(filePath);
        // add assertions to test the behavior of abrirXML method
    }

    @Test
    public void testFiltro2() {
        Filtro2 filtro2 = new Filtro2();
        File file = new File("test.jaw.xml"); // replace with appropriate file
        assertTrue(filtro2.accept(file));
        // add additional assertions if necessary
        assertEquals("XML JAW files", filtro2.getDescription());
    }

    @Test
    public void testFiltro() {
        Filtro filtro = new Filtro();
        File file = new File("test.jaw"); // replace with appropriate file
        assertTrue(filtro.accept(file));
        // add additional assertions if necessary
        assertEquals("JAW files", filtro.getDescription());
    }
}