import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;
import jaw.metamodelo.Entidade;
import jaw.gui.ProcessarEntidades;
import jaw.configuracao.Main;
import java.io.File;
import javax.swing.filechooser.FileFilter;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import java.io.FileWriter;
import jaw.xml.SerializacaoXML;
import jaw.xml.Serializacao;

public class SalvarTest {

    @Test
    public void testConstructor() {
        ProcessarEntidades form = new ProcessarEntidades();
        int tipoSalvar = 10;
        Salvar salvar = new Salvar(form, tipoSalvar);
        
        assertNotNull(salvar);
        assertFalse(salvar.vela);
        assertEquals("", salvar.arquivo);
        assertEquals("", salvar.xml);
        assertEquals(new Vector<Entidade>(), salvar.entidade);
    }

    @Test
    public void testFiltro2() {
        FileFilter filtro = new Salvar.Filtro2();

        File file = new File("test.jaw.xml");
        assertTrue(filtro.accept(file));
        assertEquals("XML JAW files", filtro.getDescription());

        File directory = new File("directory");
        assertTrue(filtro.accept(directory));

        File invalidFile = new File("test.txt");
        assertFalse(filtro.accept(invalidFile));
    }

    @Test
    public void testFiltro() {
        FileFilter filtro = new Salvar.Filtro();

        File file = new File("test.jaw");
        assertTrue(filtro.accept(file));
        assertEquals("JAW files", filtro.getDescription());

        File directory = new File("directory");
        assertTrue(filtro.accept(directory));

        File invalidFile = new File("test.txt");
        assertFalse(filtro.accept(invalidFile));
    }
}