import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;
import java.io.*;

public class SalvarTest {
    
    private static ProcessarEntidades form;
    
    @BeforeClass
    public static void setUp() {
        form = new ProcessarEntidades();
    }
    
    @Test
    public void testSalvarConstructor() {
        Salvar salvar = new Salvar(form, 10);
        assertNotNull(salvar);
    }
    
    @Test
    public void testSalvarConstructorWithException() {
        Salvar salvar = new Salvar(form, 20);
        assertNull(salvar.entidade);
        assertNull(salvar.xml);
    }
    
    @Test
    public void testSalvarConstructorWithEmptyForm() {
        ProcessarEntidades emptyForm = new ProcessarEntidades();
        Salvar salvar = new Salvar(emptyForm, 30);
        assertNull(salvar.entidade);
        assertNull(salvar.arquivo);
        assertNull(salvar.xml);
    }
    
    @Test
    public void testSalvarConstructorWithInvalidFileType() {
        Salvar salvar = new Salvar(form, 20);
        assertEquals(".jaw", salvar.arquivo.substring(salvar.arquivo.length() - 4));
    }
    
    @Test
    public void testSalvarConstructorWithInvalidXMLFileType() {
        Salvar salvar = new Salvar(form, 10);
        assertEquals(".jaw.xml", salvar.arquivo.substring(salvar.arquivo.length() - 8));
    }
    
    @Test
    public void testSalvarConstructorWithVelaSetToTrue() {
        ProcessarEntidades formWithFileName = new ProcessarEntidades();
        formWithFileName.setNomeArquivoSalvo("Novo Projeto");
        Salvar salvar = new Salvar(formWithFileName, 30);
        assertEquals(true, salvar.vela);
    }
    
    @Test
    public void testSalvarConstructorWithVelaSetToFalse() {
        Salvar salvar = new Salvar(form, 20);
        assertEquals(false, salvar.vela);
    }
    
    @Test
    public void testSalvarMethodWithValidFileType() {
        Salvar salvar = new Salvar(form, 20);
        salvar.arquivo = "test.jaw";
        salvar.salvar(form);
        File file = new File(salvar.arquivo);
        assertTrue(file.exists());
    }
    
    @Test
    public void testSalvarMethodWithInvalidFileType() {
        Salvar salvar = new Salvar(form, 20);
        salvar.arquivo = "test.txt";
        salvar.salvar(form);
        File file = new File(salvar.arquivo);
        assertFalse(file.exists());
    }
    
    @Test
    public void testSalvarMethodWithXMLFileType() {
        Salvar salvar = new Salvar(form, 10);
        salvar.arquivo = "test.jaw.xml";
        salvar.salvar(form);
        File file = new File(salvar.arquivo);
        assertTrue(file.exists());
    }
    
    @Test
    public void testSalvarMethodWithInvalidXMLFileType() {
        Salvar salvar = new Salvar(form, 10);
        salvar.arquivo = "test.xml";
        salvar.salvar(form);
        File file = new File(salvar.arquivo);
        assertFalse(file.exists());
    }
    
    @Test
    public void testSalvarMethodWithException() {
        Salvar salvar = new Salvar(form, 20);
        salvar.arquivo = "/root/test.jaw";
        salvar.salvar(form);
        String log = Main.janelaPrincipal.getLog();
        assertTrue(log.contains("FileNotFoundException"));
    }
    
    @Test
    public void testSalvarMethodWithExceptionWhenExportingXML() {
        Salvar salvar = new Salvar(form, 10);
        salvar.arquivo = "/root/test.jaw.xml";
        salvar.salvar(form);
        String log = Main.janelaPrincipal.getLog();
        assertTrue(log.contains("FileNotFoundException"));
    }
    
    @AfterClass
    public static void tearDown() {
        form = null;
    }
}