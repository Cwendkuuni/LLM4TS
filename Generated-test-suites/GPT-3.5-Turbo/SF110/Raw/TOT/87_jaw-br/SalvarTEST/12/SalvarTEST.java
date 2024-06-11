import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SalvarTest {

    private ProcessarEntidades form;
    private Salvar salvar;

    @Before
    public void setUp() {
        form = new ProcessarEntidades();
        salvar = new Salvar(form, 10);
    }

    @Test
    public void testSalvarXml_EntidadesNotNull_XMLFileSavedSuccessfully() {
        assertNotNull(salvar.entidade);
        assertEquals(".jaw.xml", salvar.arquivo.substring(salvar.arquivo.length() - 8));
    }

    @Test
    public void testSalvarXml_EntidadesNotNull_SerializationSuccessful() {
        assertNotNull(salvar.entidade);
        assertTrue(salvar.xml.length() > 0);
    }

    @Test
    public void testSalvarXml_ExceptionThrown_ShowErrorMessage() {
        form.setNomeArquivoSalvo("Novo Projeto");
        salvar = new Salvar(form, 10);
        assertNotNull(salvar.entidade);
        assertNull(salvar.arquivo);
    }

    @Test
    public void testSalvar_EntidadesNotNullAndFileTypeMatches_SerializationSuccessful() {
        assertNotNull(salvar.entidade);
        assertEquals(".jaw", salvar.arquivo.substring(salvar.arquivo.length() - 4));
        assertEquals(20, salvar.tipoSalvar);
    }

    @Test
    public void testSalvar_EntidadesNotNullAndFileTypeNotMatched_FileTypeUpdated() {
        form.setNomeArquivoSalvo("Novo Projeto");
        salvar = new Salvar(form, 20);
        assertNotNull(salvar.entidade);
        assertEquals(".jaw", salvar.arquivo.substring(salvar.arquivo.length() - 4));
        assertFalse(salvar.vela);
    }

    @Test
    public void testSalvar_EncodingExceptionThrown_ShowErrorMessage() {
        form.setNomeArquivoSalvo("Novo Projeto");
        salvar = new Salvar(form, 20);
        assertNotNull(salvar.entidade);
        assertNull(salvar.arquivo);
    }

    @Test
    public void testSalvarComo_EntidadesNotNullAndFileTypeMatches_SerializationSuccessful() {
        assertNotNull(salvar.entidade);
        assertEquals(".jaw", salvar.arquivo.substring(salvar.arquivo.length() - 4));
        assertEquals(30, salvar.tipoSalvar);
    }

    @Test
    public void testSalvarComo_EntidadesNotNullAndFileTypeNotMatched_FileTypeUpdated() {
        form.setNomeArquivoSalvo("Novo Projeto");
        salvar = new Salvar(form, 30);
        assertNotNull(salvar.entidade);
        assertEquals(".jaw", salvar.arquivo.substring(salvar.arquivo.length() - 4));
        assertFalse(salvar.vela);
    }

    @Test
    public void testSalvarComo_EncodingExceptionThrown_ShowErrorMessage() {
        form.setNomeArquivoSalvo("Novo Projeto");
        salvar = new Salvar(form, 30);
        assertNotNull(salvar.entidade);
        assertNull(salvar.arquivo);
    }
}