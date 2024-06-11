import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.lang.reflect.*;
import javax.swing.filechooser.*;

public class AbrirTest {
    private Abrir abrir;
    
    @Before
    public void setUp() {
        ProcessarEntidades form = new ProcessarEntidades();
        abrir = new Abrir(form, "testFile.jaw");
    }
    
    @After
    public void tearDown() {
        abrir = null;
    }
    
    @Test
    public void testAbrirConstructorString() {
        String expected = "";
        assertEquals(expected, abrir.xml);
        assertNotNull(abrir.form);
    }
    
    @Test
    public void testAbrirConstructorInt() {
        ProcessarEntidades form = new ProcessarEntidades();
        JFileChooser escolhedor = new JFileChooser();
        String caminho = Main.configuracao.getCaminhoDosProjetos();
        escolhedor.setCurrentDirectory(new File(caminho));
        escolhedor.setFileFilter(new Filtro2());
        escolhedor.showOpenDialog(null);
        
        if (escolhedor.getSelectedFile() != null) {
            String arquivo = escolhedor.getSelectedFile().getAbsolutePath();
            Abrir abrirXML = new Abrir(form, 10);
            assertEquals(arquivo, abrirXML.xml);
        }
    }
    
    @Test
    public void testAbrirXML() {
        String arquivo = "testFile.jaw.xml";
        ProcessarEntidades form = new ProcessarEntidades();
        SerializacaoXML serializacaoXML = new SerializacaoXML();
        Reader leitor;
        try {
            leitor = new FileReader(arquivo);
            abrir.abrirXML(arquivo);
            assertNotNull(leitor);
            assertEquals(leitor, serializacaoXML.xml2Entidades(leitor));
        } catch (Exception e) {
            String expected = "Arquivo xml corrompido ou incompatível com aplicação!";
            assertEquals(expected, e.getMessage());
        }
    }
    
    @Test
    public void testAbrirSerializado() {
        String arquivo = "testFile.jaw";
        ProcessarEntidades form = new ProcessarEntidades();
        abrir.abrirSerializado(arquivo);
        Serializacao serializacao = new Serializacao();
        try {
            assertEquals(arquivo, abrir.form.getNomeArquivoSalvo());
            assertEquals(abrir.form.getEntidades(), serializacao.abrir(arquivo));
        } catch (Exception e) {
            String expected = "Arquivo jaw corrompido ou incompatível com aplicação!";
            assertEquals(expected, e.getMessage());
        }
    }
    
    @Test
    public void testFiltro2() {
        FileFilter filtro2 = abrir.new Filtro2();
        File f = new File("testFile.jaw.xml");
        assertTrue(filtro2.accept(f));
        assertFalse(filtro2.accept(new File("testFile.jaw")));
        String expected = "XML JAW files";
        assertEquals(expected, filtro2.getDescription());
    }
    
    @Test
    public void testFiltro() {
        FileFilter filtro = abrir.new Filtro();
        File f = new File("testFile.jaw");
        assertTrue(filtro.accept(f));
        assertFalse(filtro.accept(new File("testFile.jaw.xml")));
        String expected = "JAW files";
        assertEquals(expected, filtro.getDescription());
    }
}