import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class SalvarTest {

    private Salvar salvar;

    @Before
    public void setUp() {
        salvar = new Salvar();
    }

    @Test
    public void testSalvarConstructor() {
        assertNotNull(salvar);
    }

    @Test
    public void testSalvarVelDefault() {
        assertFalse(salvar.vela);
    }

    @Test
    public void testSalvarArquivoDefault() {
        assertEquals("", salvar.arquivo);
    }

    @Test
    public void testSalvarXmlDefault() {
        assertEquals("", salvar.xml);
    }

    @Test
    public void testSalvarEntidadeEmpty() {
        assertTrue(salvar.entidade.isEmpty());
    }

    @Test
    public void testSalvarTipoSalvarDefault() {
        assertEquals(0, salvar.tipoSalvar);
    }

    @Test
    public void testSalvarTipoSalvar() {
        salvar = new Salvar(10);
        assertEquals(10, salvar.tipoSalvar);
    }

    @Test
    public void testSalvarTipoSalvarInvalid() {
        salvar = new Salvar(40);
        assertEquals(0, salvar.tipoSalvar);
    }

    @Test
    public void testSalvarTipoSalvarNegative() {
        salvar = new Salvar(-10);
        assertEquals(0, salvar.tipoSalvar);
    }

    @Test
    public void testSalvarGetEntidades() {
        assertEquals(salvar.entidades, salvar.getEntidades());
    }

    @Test
    public void testSalvarGetCaminhoDosProjetos() {
        assertEquals(Main.configuracao.getCaminhoDosProjetos(),
                     salvar.getCaminhoDosProjetos());
    }

    @Test
    public void testSalvarSetCaminhoDosProjetos() {
        final String novoCaminho = "/novo/caminho";
        salvar.setCaminhoDosProjetos(novoCaminho);
        assertEquals(novoCaminho, Main.configuracao.getCaminhoDosProjetos());
    }

    @Test
    public void testSalvarShowSaveDialog() {
        final ProcessarEntidades form = new ProcessarEntidades();
        assertNull(salvar.showSaveDialog(form));
    }

    @Test
    public void testSalvarSetNomeArquivoSalvo() {
        final String nomeArquivo = "arquivo.salvo";
        salvar.setNomeArquivoSalvo(nomeArquivo);
        assertEquals(nomeArquivo, salvar.getNomeArquivoSalvo());
    }

    @Test
    public void testSalvarGetNomeArquivoSalvo() {
        assertEquals("", salvar.getNomeArquivoSalvo());
    }

    @Test
    public void testSalvarParseEntidades() {
        final List<Entidade> entidades = new Vector<Entidade>();
        final String xml = "<?xml version=\"1.0\"?><root><entidade></entidade><entidade></entidade></root>";
        final boolean result = salvar.parseEntidades(xml);
        assertTrue(result);
        assertFalse(entidades.isEmpty());
    }

    @Test
    public void testSalvarParseEntidadesInvalid() {
        final List<Entidade> entidades = new Vector<Entidade>();
        final String xml = "<invalid>";
        final boolean result = salvar.parseEntidades(xml);
        assertFalse(result);
        assertTrue(entidades.isEmpty());
    }

    @Test
    public void testSalvarSalvarXmlSuccess() {
        final ProcessarEntidades form = new ProcessarEntidades();
        final String nomeArquivo = "arquivo";
        salvar.setNomeArquivoSalvo(nomeArquivo);
        final boolean result = salvar.salvarXml(form);
        assertTrue(result);
        assertEquals(nomeArquivo + ".jaw.xml", salvar.arquivo);
    }

    @Test
    public void testSalvarSalvarXmlInvalidNomeArquivo() {
        final ProcessarEntidades form = new ProcessarEntidades();
        final String nomeArquivo = "";
        salvar.setNomeArquivoSalvo(nomeArquivo);
        final boolean result = salvar.salvarXml(form);
        assertFalse(result);
        assertFalse(salvar.arquivo.endsWith(".jaw.xml"));
    }

    @Test
    public void testSalvarSalvarXmlException() {
        final ProcessarEntidades form = new ProcessarEntidades();
        final String nomeArquivo = "arquivo";
        salvar.setNomeArquivoSalvo(nomeArquivo);
        final boolean result = salvar.salvarXml(form);
        assertFalse(result);
    }

    @Test
    public void testSalvarSalvarSuccess() {
        final ProcessarEntidades form = new ProcessarEntidades();
        final String nomeArquivo = "arquivo";
        salvar.setNomeArquivoSalvo(nomeArquivo);
        final boolean result = salvar.salvar(form);
        assertTrue(result);
        assertEquals(nomeArquivo + ".jaw", salvar.arquivo);
    }

    @Test
    public void testSalvarSalvarInvalidNomeArquivo() {
        final ProcessarEntidades form = new ProcessarEntidades();
        final String nomeArquivo = "";
        salvar.setNomeArquivoSalvo(nomeArquivo);
        final boolean result = salvar.salvar(form);
        assertFalse(result);
        assertTrue(salvar.arquivo.endsWith(".jaw"));
    }

    @Test
    public void testSalvarSalvarException() {
        final ProcessarEntidades form = new ProcessarEntidades();
        final String nomeArquivo = "arquivo";
        salvar.setNomeArquivoSalvo(nomeArquivo);
        final boolean result = salvar.salvar(form);
        assertFalse(result);
    }
}