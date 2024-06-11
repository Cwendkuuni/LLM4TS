import static org.junit.Assert.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AbrirTest {
    
    private Abrir abrir;
    private ProcessarEntidades form;
    
    @Before
    public void setUp() throws Exception {
        form = new ProcessarEntidades();
        abrir = new Abrir(form, "testFile.xml");
    }
    
    @After
    public void tearDown() throws Exception {
        form = null;
        abrir = null;
    }
    
    @Test
    public void testAbrirConstructorWithXML() {
        abrir = new Abrir(form, "testFile.jaw.xml");
        assertNotNull(abrir);
        assertEquals("testFile.jaw.xml", abrir.xml);
        assertEquals(form, abrir.form);
    }
    
    @Test
    public void testAbrirConstructorWithSerializado() {
        abrir = new Abrir(form, "testFile.jaw");
        assertNotNull(abrir);
        assertEquals("testFile.jaw", abrir.xml);
        assertEquals(form, abrir.form);
    }

    @Test
    public void testAbrirConstructorWithInvalidFile() {
        abrir = new Abrir(form, "testFile.txt");
        assertNotNull(abrir);
        assertEquals("", abrir.xml);
        assertEquals(form, abrir.form);
    }
    
    @Test
    public void testAbrirConstructorWithABRIR_XML() {
        abrir = new Abrir(form, Abrir.ABRIR_XML);
        assertNotNull(abrir);
        assertEquals("", abrir.xml);
        assertEquals(form, abrir.form);
    }
    
    @Test
    public void testAbrirConstructorWithABRIR() {
        abrir = new Abrir(form, Abrir.ABRIR);
        assertNotNull(abrir);
        assertEquals("", abrir.xml);
        assertEquals(form, abrir.form);
    }
    
    @Test
    public void testAbrirConstructorWithInvalidTipoAbrir() {
        abrir = new Abrir(form, 30);
        assertNotNull(abrir);
        assertEquals("", abrir.xml);
        assertEquals(form, abrir.form);
    }
    
    @Test
    public void testAbrirSerializadoWithValidFile() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
        abrir.abrirSerializado("testFile.jaw");
        assertEquals("Arquivo jaw corrompido ou incompatível com aplicação!\n", outContent.toString());
        assertNotNull(abrir.form.getEntidades());
        
        System.setOut(System.out);
    }
    
    @Test
    public void testAbrirSerializadoWithInvalidFile() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
        abrir.abrirSerializado("testFile.txt");
        assertEquals("Arquivo jaw corrompido ou incompatível com aplicação!\n", outContent.toString());
        
        System.setOut(System.out);
    }
    
    @Test
    public void testAbrirXMLWithValidFile() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
        abrir.abrirXML("testFile.jaw.xml");
        assertEquals("Arquivo xml corrompido ou incompatível com aplicação!\n", outContent.toString());
        assertNotNull(abrir.form.getEntidades());
        
        System.setOut(System.out);
    }
    
    @Test
    public void testAbrirXMLWithInvalidFile() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
        abrir.abrirXML("testFile.txt");
        assertEquals("Arquivo xml corrompido ou incompatível com aplicação!\n", outContent.toString());
        
        System.setOut(System.out);
    }
    
    @Test
    public void testFiltro2AcceptWithValidFile() {
        Filtro2 filtro2 = new Filtro2();
        assertTrue(filtro2.accept(new File("testFile.jaw.xml")));
    }
    
    @Test
    public void testFiltro2AcceptWithInvalidFile() {
        Filtro2 filtro2 = new Filtro2();
        assertFalse(filtro2.accept(new File("testFile.jaw")));
    }
    
    @Test
    public void testFiltro2GetDescription() {
        Filtro2 filtro2 = new Filtro2();
        assertEquals("XML JAW files", filtro2.getDescription());
    }
    
    @Test
    public void testFiltroAcceptWithValidFile() {
        Filtro filtro = new Filtro();
        assertTrue(filtro.accept(new File("testFile.jaw")));
    }
    
    @Test
    public void testFiltroAcceptWithInvalidFile() {
        Filtro filtro = new Filtro();
        assertFalse(filtro.accept(new File("testFile.jaw.xml")));
    }
    
    @Test
    public void testFiltroGetDescription() {
        Filtro filtro = new Filtro();
        assertEquals("JAW files", filtro.getDescription());
    }
    
    // edge case: escolhedor with null selected file
    @Test
    public void testSecondConstructorWithNullSelectedFile() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
        abrir = new Abrir(form, Abrir.ABRIR_XML);
        assertEquals("", abrir.xml);
        assertEquals(form, abrir.form);
        
        System.setOut(System.out);
    
    }
}