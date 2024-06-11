import static org.junit.Assert.*;
import org.junit.Test;
import java.io.File;
import java.io.FileWriter;
import java.util.List;
import java.util.Vector;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

public class SalvarTest {

    private Salvar salvar;

    @Test
    public void testSalvarConstructor() {
        ProcessarEntidades form = new ProcessarEntidades();
        int tipoSalvar = 10;
        salvar = new Salvar(form, tipoSalvar);
        List<Entidade> expectedEntidade = new Vector<Entidade>();
        expectedEntidade = form.getEntidades();
        List<Entidade> actualEntidade = salvar.entidade;
        assertEquals(expectedEntidade, actualEntidade);
    }

    @Test
    public void testSalvarXML() {
        ProcessarEntidades form = new ProcessarEntidades();
        int tipoSalvar = 10;
        salvar = new Salvar(form, tipoSalvar);
        String expectedXML = salvar.xml;
        String actualXML = salvar.xml;
        assertEquals(expectedXML, actualXML);
    }

    @Test
    public void testSalvarArquivo() throws Exception {
        ProcessarEntidades form = new ProcessarEntidades();
        int tipoSalvar = 20;
        salvar = new Salvar(form, tipoSalvar);
        String expectedArquivo = form.getNomeArquivoSalvo();
        String actualArquivo = salvar.arquivo;
        assertEquals(expectedArquivo, actualArquivo);
    }

    @Test
    public void testSalvarArquivoComNovoProjeto() throws Exception {
        ProcessarEntidades form = new ProcessarEntidades();
        String expectedNomeArquivo = "Novo Projeto.jaw";
        form.setNomeArquivoSalvo(expectedNomeArquivo);
        int tipoSalvar = 20;
        salvar = new Salvar(form, tipoSalvar);
        String expectedArquivo = expectedNomeArquivo;
        String actualArquivo = salvar.arquivo;
        assertEquals(expectedArquivo, actualArquivo);
    }

    @Test
    public void testSalvarArquivoComNovoProjetoInvalidFileFormat() throws Exception {
        ProcessarEntidades form = new ProcessarEntidades();
        String expectedNomeArquivo = "Novo Projeto.csv";
        form.setNomeArquivoSalvo(expectedNomeArquivo);
        int tipoSalvar = 20;
        salvar = new Salvar(form, tipoSalvar);
        String expectedArquivo = expectedNomeArquivo + ".jaw";
        String actualArquivo = salvar.arquivo;
        assertEquals(expectedArquivo, actualArquivo);
    }

    @Test
    public void testSalvarArquivoWithValidFileFormat() throws Exception {
        ProcessarEntidades form = new ProcessarEntidades();
        String expectedNomeArquivo = "Projeto";
        form.setNomeArquivoSalvo(expectedNomeArquivo);
        int tipoSalvar = 30;
        salvar = new Salvar(form, tipoSalvar);
        String expectedArquivo = expectedNomeArquivo + ".jaw";
        String actualArquivo = salvar.arquivo;
        assertEquals(expectedArquivo, actualArquivo);
    }

    @Test
    public void testSalvarArquivoWithInvalidFileFormat() throws Exception {
        ProcessarEntidades form = new ProcessarEntidades();
        String expectedNomeArquivo = "Projeto.docx";
        form.setNomeArquivoSalvo(expectedNomeArquivo);
        int tipoSalvar = 30;
        salvar = new Salvar(form, tipoSalvar);
        String expectedArquivo = expectedNomeArquivo;
        String actualArquivo = salvar.arquivo;
        assertEquals(expectedArquivo, actualArquivo);
    }

    @Test
    public void testSalvarXMLFile() {
        ProcessarEntidades form = new ProcessarEntidades();
        int tipoSalvar = 10;
        salvar = new Salvar(form, tipoSalvar);
        SerializacaoXML serializaXML = new SerializacaoXML();
        String expectedXML = serializaXML.entidades2XML(salvar.entidade);
        String actualXML = salvar.xml;
        assertEquals(expectedXML, actualXML);
    }

    @Test
    public void testSalvarXMLFileException() {
        ProcessarEntidades form = new ProcessarEntidades();
        int tipoSalvar = 10;
        salvar = new Salvar(form, tipoSalvar);
        String expectedMessage = "NÃ£o foi possÃ­vel salvar esse arquivo\n Verifique o formato das entidades !";
        String actualMessage = "";
        try {
            FileWriter escrevedor = new FileWriter(salvar.arquivo);
            escrevedor.write(salvar.xml);
            escrevedor.flush();
            escrevedor.close();
        } catch (Exception e) {
            actualMessage = expectedMessage;
        }
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testFileFilterJAWXML() {
        Salvar.Filtro2 filtro = new Salvar.Filtro2();
        File file = new File("file.jaw.xml");
        boolean expected = true;
        boolean actual = filtro.accept(file);
        assertEquals(expected, actual);
    }

    @Test
    public void testFileFilterJAW() {
        Salvar.Filtro filtro = new Salvar.Filtro();
        File file = new File("file.jaw");
        boolean expected = true;
        boolean actual = filtro.accept(file);
        assertEquals(expected, actual);
    }

    @Test
    public void testFileFilterDescriptionJAWXML() {
        Salvar.Filtro2 filtro = new Salvar.Filtro2();
        String expectedDescription = "XML JAW files";
        String actualDescription = filtro.getDescription();
        assertEquals(expectedDescription, actualDescription);
    }

    @Test
    public void testFileFilterDescriptionJAW() {
        Salvar.Filtro filtro = new Salvar.Filtro();
        String expectedDescription = "JAW files";
        String actualDescription = filtro.getDescription();
        assertEquals(expectedDescription, actualDescription);
    }
}