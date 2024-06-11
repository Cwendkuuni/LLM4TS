import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import jaw.entrada.Abrir;
import jaw.gui.ProcessarEntidades;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

public class AbrirTest {
    
    @Mock
    ProcessarEntidades form = Mockito.mock(ProcessarEntidades.class);
    
    @Test
    public void testConstructorWithArquivoEndingWithXML() {
        Abrir abrir = new Abrir(form, "test.jaw.xml");
        assertNotNull(abrir.xml);
        assertNotNull(abrir.form);
    }
    
    @Test
    public void testConstructorWithArquivoEndingWithJAW() {
        Abrir abrir = new Abrir(form, "test.jaw");
        assertNotNull(abrir.xml);
        assertNotNull(abrir.form);
    }
    
    @Test
    public void testConstructorWithTipoAbrirTen() {
         Abrir abrir = new Abrir(form, Abrir.ABRIR_XML);
         assertNotNull(abrir.xml);
         assertNotNull(abrir.form);
    }
    
    @Test
    public void testConstructorWithTipoAbrirTwenty() {
         Abrir abrir = new Abrir(form, Abrir.ABRIR);
         assertNotNull(abrir.xml);
         assertNotNull(abrir.form);
    }
    
    @Test
    public void testAbrirSerializadoWithIncompatibleJAWFile() {
        Abrir abrir = new Abrir(form, "test.incompatible.jaw");
        assertNotNull(abrir.xml);
        assertNotNull(abrir.form);
    }
    
    @Test
    public void testAbrirXMLWithIncompatibleXMLFile() {
        Abrir abrir = new Abrir(form, "test.incompatible.xml");
        assertNotNull(abrir.xml);
        assertNotNull(abrir.form);
    }
    
    @Test
    public void testFiltro2Accept() {
        Abrir.Filtro2 filtro = new Abrir().new Filtro2();
        assertTrue(filtro.accept(new File("test.jaw.xml")));
        assertFalse(filtro.accept(new File("test.txt")));
    }
    
    @Test
    public void testFiltroDescription() {
        Abrir.Filtro2 filtro = new Abrir().new Filtro();
        assertEquals("XML JAW files", filtro.getDescription());
    }
}