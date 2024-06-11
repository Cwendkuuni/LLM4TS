import org.junit.Test;
import static org.junit.Assert.*;
import jaw.ProcessarEntidades;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import java.io.File;

public class AbrirTest {
    private ProcessarEntidades form = new ProcessarEntidades();

    @Test
    public void testAbrirXML() {
        Abrir abrir = new Abrir(form, "exampleFile.jaw.xml");
        assertNotNull(abrir.form);
        assertNotNull(abrir.xml);
        assertEquals("exampleFile.jaw.xml", abrir.form.getNomeArquivoSalvo());
        // Add edge cases and exception handling tests here
    }

    @Test
    public void testAbrirSerializado() {
        Abrir abrir = new Abrir(form, 20);
        assertNotNull(abrir.form);
        assertNotNull(abrir.xml);
        // Add edge cases and exception handling tests here
    }

    @Test
    public void testFiltro2() {
        Abrir.Filtro2 filtro = new Abrir.Filtro2();
        assertTrue(filtro.accept(new File("testFile.jaw.xml")));
        assertFalse(filtro.accept(new File("testFile.txt")));
        assertEquals("XML JAW files", filtro.getDescription());
    }

    @Test
    public void testFiltro() {
        Abrir.Filtro filtro = new Abrir.Filtro();
        assertTrue(filtro.accept(new File("testFile.jaw")));
        assertFalse(filtro.accept(new File("testFile.txt")));
        assertEquals("JAW files", filtro.getDescription());
    }
}