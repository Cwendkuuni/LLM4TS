package jaw.entrada;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import jaw.ProcessarEntidades;

public class SalvarTest {

    private Salvar salvar;

    @Before
    public void setUp() {
        ProcessarEntidades form = new ProcessarEntidades();
        salvar = new Salvar(form, 10);
    }

    @Test
    public void testSalvarConstructor() {
        ProcessarEntidades form = new ProcessarEntidades();
        Salvar salvar = new Salvar(form, 10);
        assertEquals(false, salvar.vela);
        assertEquals("", salvar.arquivo);
        assertEquals("", salvar.xml);
        assertEquals(0, salvar.entidade.size());
    }

    @Test
    public void testSalvar() {
        Salvar salvar = new Salvar();
        assertFalse(salvar.vela);
        assertEquals("", salvar.arquivo);
        assertEquals("", salvar.xml);
        assertEquals(0, salvar.entidade.size());
    }

    @Test
    public void testSALVAR_XML() {
        assertEquals(10, Salvar.SALVAR_XML);
    }

    @Test
    public void testSALVAR() {
        assertEquals(20, Salvar.SALVAR);
    }

    @Test
    public void testSALVAR_COMO() {
        assertEquals(30, Salvar.SALVAR_COMO);
    }

    @Test
    public void testSalvarNomeArquivoSalvoNull() {
        ProcessarEntidades form = new ProcessarEntidades();
        form.setNomeArquivoSalvo(null);
        Salvar salvar = new Salvar(form, 20);
        assertEquals(true, salvar.vela);
        assertEquals("", salvar.arquivo);
    }

    @Test
    public void testSalvarNomeArquivoSalvoNovoProjeto() {
        ProcessarEntidades form = new ProcessarEntidades();
        form.setNomeArquivoSalvo("Novo Projeto");
        Salvar salvar = new Salvar(form, 20);
        assertEquals(true, salvar.vela);
        assertEquals("", salvar.arquivo);
    }

    @Test
    public void testSalvarNomeArquivoSalvoNotEmpty() {
        ProcessarEntidades form = new ProcessarEntidades();
        form.setNomeArquivoSalvo("Test File");
        Salvar salvar = new Salvar(form, 20);
        assertEquals(false, salvar.vela);
        assertEquals("Test File", salvar.arquivo);
    }

    @Test
    public void testSalvarTipoSalvar10() {
        ProcessarEntidades form = new ProcessarEntidades();
        form.setNomeArquivoSalvo("Test File");
        Salvar salvar = new Salvar(form, 10);
        assertEquals(true, salvar.vela);
        assertEquals("Test File.jaw.xml", salvar.arquivo);
    }

    @Test
    public void testSalvarTipoSalvar20() {
        ProcessarEntidades form = new ProcessarEntidades();
        form.setNomeArquivoSalvo("Test File");
        Salvar salvar = new Salvar(form, 20);
        assertEquals(false, salvar.vela);
        assertEquals("Test File.jaw", salvar.arquivo);
    }

    @Test
    public void testSalvarTipoSalvar30() {
        ProcessarEntidades form = new ProcessarEntidades();
        form.setNomeArquivoSalvo("Test File");
        Salvar salvar = new Salvar(form, 30);
        assertEquals(true, salvar.vela);
        assertEquals("Test File.jaw", salvar.arquivo);
    }

    @Test
    public void testSalvarTipoSalvarInvalid() {
        ProcessarEntidades form = new ProcessarEntidades();
        form.setNomeArquivoSalvo("Test File");
        Salvar salvar = new Salvar(form, 40);
        assertEquals(false, salvar.vela);
        assertEquals("Test File", salvar.arquivo);
    }

}