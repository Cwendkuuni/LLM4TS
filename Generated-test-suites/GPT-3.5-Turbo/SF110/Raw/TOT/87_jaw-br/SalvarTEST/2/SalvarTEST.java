import static org.junit.Assert.*;
import org.junit.*;
import java.io.*;
import jaw.entrada.Salvar;
import jaw.metamodelo.Entidade;
import java.util.List;

public class SalvarTest {

    @Test
    public void testSalvarConstructor() {
        ProcessarEntidades form = new ProcessarEntidades();
        int tipoSalvar = 10;
        Salvar salvar = new Salvar(form, tipoSalvar);
        assertEquals(false, salvar.vela);
        assertEquals("", salvar.arquivo);
        assertEquals("", salvar.xml);
        assertNotNull(salvar.entidade);
    }

    @Test
    public void testSalvarXML() {
        ProcessarEntidades form = new ProcessarEntidades();
        int tipoSalvar = 10;
        Salvar salvar = new Salvar(form, tipoSalvar);
        salvar.entidade.add(new Entidade());
        salvar.xml = salvar.serializaXML.entidades2XML(salvar.entidade);
        assertFalse(salvar.xml.isEmpty());

        try {
            FileWriter escrevedor = new FileWriter("test.jaw.xml");
            escrevedor.write(salvar.xml);
            escrevedor.flush();
            escrevedor.close();
            assertTrue(new File("test.jaw.xml").exists());
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testSalvarXMLWithInvalidEntities() {
        ProcessarEntidades form = new ProcessarEntidades();
        int tipoSalvar = 10;
        Salvar salvar = new Salvar(form, tipoSalvar);
        salvar.xml = salvar.serializaXML.entidades2XML(salvar.entidade);
        assertFalse(salvar.xml.isEmpty());

        try {
            FileWriter escrevedor = new FileWriter("test.jaw.xml");
            escrevedor.write(salvar.xml);
            escrevedor.flush();
            escrevedor.close();
            fail("IOException not thrown");
        } catch (IOException e) {
            assertNotNull(e);
        }
    }

    @Test
    public void testSalvar() {
        ProcessarEntidades form = new ProcessarEntidades();
        int tipoSalvar = 20;
        Salvar salvar = new Salvar(form, tipoSalvar);
        salvar.entidade.add(new Entidade());
        salvar.arquivo = "test.jaw";
        assertTrue(salvar.arquivo.endsWith(".jaw"));

        try {
            Serializacao salvarObj = new Serializacao();
            assertTrue(salvarObj.salvar(salvar.entidade, salvar.arquivo));
            assertTrue(new File("test.jaw").exists());
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

    @Test
    public void testSalvarWithInvalidEntities() {
        ProcessarEntidades form = new ProcessarEntidades();
        int tipoSalvar = 20;
        Salvar salvar = new Salvar(form, tipoSalvar);
        salvar.arquivo = "test.jaw";
        assertTrue(salvar.arquivo.endsWith(".jaw"));

        try {
            Serializacao salvarObj = new Serializacao();
            assertFalse(salvarObj.salvar(salvar.entidade, salvar.arquivo));
            assertFalse(new File("test.jaw").exists());
        } catch (IOException e) {
            assertNotNull(e);
        }
    }

    @Test
    public void testSalvarComoWithNovoProjeto() {
        ProcessarEntidades form = new ProcessarEntidades();
        form.setNomeArquivoSalvo("Novo Projeto");
        int tipoSalvar = 30;
        Salvar salvar = new Salvar(form, tipoSalvar);
        assertEquals(true, salvar.vela);
        assertEquals("", salvar.arquivo);

        JFileChooser escolhedor = salvar.escolhedor;
        File selectedFile = new File("test.jaw");
        escolhedor.setSelectedFile(selectedFile);
        assertEquals(selectedFile.getAbsolutePath(), salvar.arquivo);
    }

    @Test
    public void testSalvarComoWithExistingFile() {
        ProcessarEntidades form = new ProcessarEntidades();
        form.setNomeArquivoSalvo("Existing File");
        int tipoSalvar = 30;
        Salvar salvar = new Salvar(form, tipoSalvar);
        assertEquals(false, salvar.vela);
        assertEquals("Existing File", salvar.arquivo);

        JFileChooser escolhedor = salvar.escolhedor;
        File selectedFile = new File("test.jaw");
        escolhedor.setSelectedFile(selectedFile);
        assertEquals(selectedFile.getAbsolutePath(), salvar.arquivo);
    }

}