package jaw.entrada;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import javax.swing.*;
import java.io.*;

public class AbrirTest {
    
    @Test
    public void testConstructorArquivoXML() {
        ProcessarEntidades form = new ProcessarEntidades();
        
        Abrir abrir = new Abrir(form, "xml.jaw.xml");
        
        assertEquals("", abrir.xml);
        assertEquals(form, abrir.form);
        
        // Test abrirXML method
    }
    
    @Test
    public void testConstructorArquivoSerializado() {
        ProcessarEntidades form = new ProcessarEntidades();
        
        Abrir abrir = new Abrir(form, "serial.jaw");
        
        assertEquals("", abrir.xml);
        assertEquals(form, abrir.form);
        
        // Test abrirSerializado method
    }
    
    @Test
    public void testConstructorTipoAbrirABRIR_XML() {
        ProcessarEntidades form = new ProcessarEntidades();
        
        Abrir abrir = new Abrir(form, Abrir.ABRIR_XML);
        
        assertEquals("", abrir.xml);
        assertEquals(form, abrir.form);
        
        JFileChooser escolhedor = new JFileChooser();
        assertNull(escolhedor.getCurrentDirectory());
        assertEquals(new Filtro2(), escolhedor.getFileFilter());
        // Test showOpenDialog null
    }
    
    @Test
    public void testConstructorTipoAbrirABRIR() {
        ProcessarEntidades form = new ProcessarEntidades();
        
        Abrir abrir = new Abrir(form, Abrir.ABRIR);
        
        assertEquals("", abrir.xml);
        assertEquals(form, abrir.form);
        
        JFileChooser escolhedor = new JFileChooser();
        assertNull(escolhedor.getCurrentDirectory());
        assertEquals(new Filtro(), escolhedor.getFileFilter());
        // Test showOpenDialog null
    }
    
    @Test
    public void testAbrirSerializado() {
        ProcessarEntidades form = new ProcessarEntidades();
        Abrir abrir = new Abrir(form, 10);
        
        abrir.abrirSerializado("serial.jaw");
        
        // Test setNomeArquivoSalvo
        // Test setEntidades
        
        // Test if statement of method
        
    }
    
    @Test
    public void testAbrirSerializadoInvalidFile() {
        ProcessarEntidades form = new ProcessarEntidades();
        Abrir abrir = new Abrir(form, 10);
        
        abrir.abrirSerializado("file.txt");
        
        // Test showOpenDialog null
        // Test showMessageDialog
    }

    @Test
    public void testAbrirXML() {
        ProcessarEntidades form = new ProcessarEntidades();
        Abrir abrir = new Abrir(form, 20);
        
        abrir.abrirXML("xml.jaw.xml");
        
        // Test setNomeArquivoSalvo
        // Test xml2Entidades
        
        // Test if statement of method
    }
    
    @Test
    public void testAbrirXMLInvalidFile() {
        ProcessarEntidades form = new ProcessarEntidades();
        Abrir abrir = new Abrir(form, 20);
        
        abrir.abrirXML("file.txt");
        
        // Test showOpenDialog null
        // Test showMessageDialog
    }
    
    @Test
    public void testFiltroAccept() {
        FileFilter filtro = new Abrir.Filtro();
        
        File file = new File("file.jaw");
        File directory = new File("");
        
        assertEquals(true, filtro.accept(file));
        assertEquals(true, filtro.accept(directory));
    }
    
    @Test
    public void testFiltroGetDescription() {
        FileFilter filtro = new Abrir.Filtro();
        
        assertEquals("JAW files", filtro.getDescription());
    }
    
    @Test
    public void testFiltro2Accept() {
        FileFilter filtro = new Abrir.Filtro2();
        
        File file = new File("file.jaw.xml");
        File directory = new File("");
        
        assertEquals(true, filtro.accept(file));
        assertEquals(true, filtro.accept(directory));
    }
    
    @Test
    public void testFiltro2GetDescription() {
        FileFilter filtro = new Abrir.Filtro2();
        
        assertEquals("XML JAW files", filtro.getDescription());
    }
    
}