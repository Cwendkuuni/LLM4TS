package jaw.entrada;

import static org.junit.Assert.*;
import jaw.gui.*;
import jaw.*;
import javax.swing.filechooser.*;
import java.awt.*;
import javax.swing.*;
import java.io.*;
import org.junit.Test;

public class AbrirTest {
    
    @Test
    public void testAbrirConstructor1() {
        ProcessarEntidades form = new ProcessarEntidades();
        Abrir abrir = new Abrir(form, "test.jaw.xml");
        
        assertEquals("", abrir.xml);
        assertEquals(form, abrir.form);
        //Assert other assertions for constructor 1
    }
    
    @Test
    public void testAbrirConstructor2() {
        ProcessarEntidades form = new ProcessarEntidades();
        Abrir abrir = new Abrir(form, 10);
        
        assertEquals("", abrir.xml);
        assertEquals(form, abrir.form);
        //Assert other assertions for constructor 2
    }
    
    @Test
    public void testAbrirSerializado() {
        ProcessarEntidades form = new ProcessarEntidades();
        Abrir abrir = new Abrir(form, "test.jaw");
        
        assertEquals("", abrir.xml);
        assertEquals(form, abrir.form);
        //Assert assertions for abrirSerializado method
    }
    
    @Test
    public void testAbrirXML() {
        ProcessarEntidades form = new ProcessarEntidades();
        Abrir abrir = new Abrir(form, "test.jaw.xml");
        
        assertEquals("", abrir.xml);
        assertEquals(form, abrir.form);
        //Assert assertions for abrirXML method
    }
    
    //Test Filtro2 class
    @Test
    public void testFiltro2Accept() {
        FileFilter filtro = new Filtro2();
        File file = new File("test.jaw.xml");
        
        assertTrue(filtro.accept(file));
    }
    
    @Test
    public void testFiltro2AcceptFalse() {
        FileFilter filtro = new Filtro2();
        File file = new File("test.jaw");
        
        assertFalse(filtro.accept(file));
    }
    
    @Test
    public void testFiltro2Description() {
        FileFilter filtro = new Filtro2();
        
        assertEquals("XML JAW files", filtro.getDescription());
    }
    
    //Test Filtro class
    @Test
    public void testFiltroAccept() {
        FileFilter filtro = new Filtro();
        File file = new File("test.jaw");
        
        assertTrue(filtro.accept(file));
    }
    
    @Test
    public void testFiltroAcceptFalse() {
        FileFilter filtro = new Filtro();
        File file = new File("test.jaw.xml");
        
        assertFalse(filtro.accept(file));
    }
    
    @Test
    public void testFiltroDescription() {
        FileFilter filtro = new Filtro();
        
        assertEquals("JAW files", filtro.getDescription());
    }
}