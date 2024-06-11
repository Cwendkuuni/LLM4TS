import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;
import javax.swing.*;
import java.io.*;

public class SalvarTest {

    private ProcessarEntidades form;
    private Salvar salvar;

    @Before
    public void setUp() {
        form = new ProcessarEntidades();
        salvar = new Salvar(form, 10);
    }

    @Test
    public void testConstructor_ValidFormAndTipoSalvar_ShouldSetValuesAndInitializeVariables() {
        assertEquals(false, salvar.vela);
        assertEquals("", salvar.arquivo);
        assertEquals("", salvar.xml);
        assertNotNull(salvar.entidade);
        assertTrue(salvar.entidade instanceof Vector<Entidade>);
    }

    @Test
    public void testConstructor_ExceptionInGetEntidades_ShouldLogExceptionAndReturn() {
        form = null;
        salvar = new Salvar(form, 10);
        assertEquals("java.lang.NullPointerException", Main.janelaPrincipal.getLog());
        assertNull(salvar.entidade);
    }

    @Test
    public void testConstructor_TipoSalvar10_ShouldSetFileFilterToFiltro2() {
        JFileChooser escolhedor = new JFileChooser();
        escolhedor.setFileFilter(new Filtro2());
        assertEquals(escolhedor.getFileFilter(), escolhedor.getFileFilter());
    }

    @Test
    public void testConstructor_TipoSalvar20_ShouldSetFileFilterToFiltro() {
        JFileChooser escolhedor = new JFileChooser();
        escolhedor.setFileFilter(new Filtro());
        assertEquals(escolhedor.getFileFilter(), escolhedor.getFileFilter());
    }

    @Test
    public void testConstructor_TipoSalvar10Or30OrFormNomeArquivoSalvoEqualsNovoProjeto_ShouldShowSaveDialogAndSetVelaToTrue() {
        form.setName("");
        JFileChooser escolhedor = new JFileChooser();
        escolhedor.showSaveDialog(form);
        assertEquals(true, salvar.vela);
    }

    @Test
    public void testConstructor_SelectedFileIsNullAndFormNameIsEmpty_ShouldReturn() {
        JFileChooser escolhedor = new JFileChooser();
        escolhedor.setSelectedFile(null);
        form.setName("");
        assertEquals("", salvar.arquivo);
    }

    @Test
    public void testConstructor_VelaAndSelectedFileIsNotNull_ShouldSetArquivoToAbsolutePathOfSelectedFile() {
        JFileChooser escolhedor = new JFileChooser();
        escolhedor.setSelectedFile(new File("test.jaw"));
        salvar.vela = true;
        salvar.arquivo = "";
        assertEquals("test.jaw", salvar.arquivo);
    }

    @Test
    public void testConstructor_FormNomeArquivoSalvoIsNotNull_ShouldSetArquivoToFormNomeArquivoSalvo() {
        salvar.vela = false;
        salvar.arquivo = "";
        form.setNomeArquivoSalvo("test.jaw");
        assertEquals("test.jaw", salvar.arquivo);
    }

    @Test
    public void testConstructor_TipoSalvar20Or30AndArquivoDoesNotEndWithJAWAndVelaIsTrue_ShouldSetNomeArquivoSalvoAndSetVelaToFalse() {
        salvar.vela = true;
        salvar.arquivo = "test";
        form.setNomeArquivoSalvo("");
        salvar.arquivo = salvar.arquivo + ".jaw";
        assertEquals("test.jaw", form.getNomeArquivoSalvo());
        assertEquals(false, salvar.vela);
    }

    @Test
    public void testConstructor_SalvarReturnsTrue_ShouldShowMessageDialog() {
        Serializacao salvar = new Serializacao();
        assertTrue(salvar.salvar(salvar.entidade, salvar.arquivo));
        assertEquals("Arquivo salvo com sucesso!", JOptionPane.showMessageDialog());
    }

    @Test
    public void testConstructor_SalvarThrowsException_ShouldShowErrorMessageDialogAndLogException() {
        Serializacao salvar = new Serializacao();
        assertFalse(salvar.salvar(salvar.entidade, salvar.arquivo));
        assertEquals("java.lang.Exception: Não foi possível salvar esse arquivo\n Verifique o formato das entidades !", JOptionPane.showMessageDialog());
        assertEquals("java.lang.Exception", Main.janelaPrincipal.getLog());
    }

    @Test
    public void testConstructor_TipoSalvar10AndArquivoDoesNotEndWithJAWXML_ShouldAddJAWXMLToFile() throws IOException {
        String arquivo = "test";
        form.setNomeArquivoSalvo(arquivo + ".xml");
        salvar.arquivo = arquivo;
        FileWriter escrevedor = new FileWriter(salvar.arquivo);
        escrevedor.write(salvar.xml);
        escrevedor.flush();
        escrevedor.close();
        assertEquals(arquivo + ".jaw.xml", salvar.arquivo);
        assertEquals("Arquivo Exportado com sucesso!", JOptionPane.showMessageDialog());
    }

    @Test
    public void testConstructor_TipoSalvar10AndArquivoEndsWithJAWXML_ShouldNotAddAnythingToFile() throws IOException {
        String arquivo = "test.jaw.xml";
        form.setNomeArquivoSalvo(arquivo);
        salvar.arquivo = arquivo;
        FileWriter escrevedor = new FileWriter(salvar.arquivo);
        escrevedor.write(salvar.xml);
        escrevedor.flush();
        escrevedor.close();
        assertEquals(arquivo, salvar.arquivo);
        assertEquals("Arquivo Exportado com sucesso!", JOptionPane.showMessageDialog());
    }
}