import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.*;
import javax.swing.*;
import javax.swing.filechooser.*;
import java.io.*;

public class SalvarTest {
    
    private ProcessarEntidades form;
    private Salvar salvarXML;
    private Salvar salvar;
    private Salvar salvarComo;
    
    @Before
    public void setUp() {
        form = new ProcessarEntidades();
        salvarXML = new Salvar(form, 10);
        salvar = new Salvar(form, 20);
        salvarComo = new Salvar(form, 30);
    }
    
    @Test
    public void testSalvarXML() {
        File expectedFile = new File("testfile.jaw.xml");
        
        assertEquals(expectedFile.getName().toLowerCase(), salvarXML.arquivo.toLowerCase());
        assertEquals(10, salvarXML.tipoSalvar);
        
        // Check if file has .xml extension
        assertTrue(expectedFile.getName().endsWith(".jaw.xml"));
        
        // check if file exists
        assertTrue(expectedFile.exists());
        
        // Check if file is successfully saved with serialized data
        String xmlData = salvarXML.xml;
        assertNotNull(xmlData);
    }
    
    @Test
    public void testSalvar() {
        File expectedFile = new File("testfile.jaw");
        
        assertEquals(expectedFile.getName().toLowerCase(), salvar.arquivo.toLowerCase());
        assertEquals(20, salvar.tipoSalvar);
        
        // Check if file does not have .xml extension
        assertFalse(expectedFile.getName().endsWith(".jaw.xml"));
        
        // check if file exists
        assertTrue(expectedFile.exists());
        
        // Check if file is successfully saved with serialized data
        String xmlData = salvar.xml;
        assertNotNull(xmlData);
    }
    
    @Test
    public void testSalvarComo() {
        File expectedFile = new File("testfile.jaw");
        
        assertEquals(expectedFile.getName().toLowerCase(), salvarComo.arquivo.toLowerCase());
        assertEquals(30, salvarComo.tipoSalvar);
        
        // Check if file does not have .xml extension
        assertFalse(expectedFile.getName().endsWith(".jaw.xml"));
        
        // check if file exists
        assertTrue(expectedFile.exists());
        
        // Check if file is successfully saved with serialized data
        String xmlData = salvarComo.xml;
        assertNotNull(xmlData);
    }
    
    @Test
    public void testFiltro2() {
        Salvar.Filtro2 filtro2 = salvar.new Filtro2();
        
        File xmlFile = new File("testfile.jaw.xml");
        assertTrue(filtro2.accept(xmlFile));
        
        File jawFile = new File("testfile.jaw");
        assertFalse(filtro2.accept(jawFile));
        
        File directory = new File("testdirectory");
        assertTrue(filtro2.accept(directory));
        
        assertEquals("XML JAW files", filtro2.getDescription());
    }
    
    @Test
    public void testFiltro() {
        Salvar.Filtro filtro = salvar.new Filtro();
        
        File xmlFile = new File("testfile.jaw.xml");
        assertFalse(filtro.accept(xmlFile));
        
        File jawFile = new File("testfile.jaw");
        assertTrue(filtro.accept(jawFile));
        
        File directory = new File("testdirectory");
        assertTrue(filtro.accept(directory));
        
        assertEquals("JAW files", filtro.getDescription());
    }
    
    @Test
    public void testSalvarWithEmptyFormSelectedFileNull() {
        ProcessarEntidades emptyForm = new ProcessarEntidades();
        Salvar salvarWithEmptyForm = new Salvar(emptyForm, 10);
        
        assertNull(salvarWithEmptyForm.arquivo);
        assertEquals(10, salvarWithEmptyForm.tipoSalvar);
    }
    
    @Test
    public void testSalvarWithEmptyFormSelectedFileNotNull() {
        ProcessarEntidades emptyForm = new ProcessarEntidades();
        emptyForm.setName("Test Name");
        Salvar salvarWithEmptyForm = new Salvar(emptyForm, 20);
        
        assertNull(salvarWithEmptyForm.arquivo);
        assertEquals(20, salvarWithEmptyForm.tipoSalvar);
    }
}