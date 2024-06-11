import org.junit.*;
import static org.junit.Assert.*;

public class SalvarTest {

    @Test
    public void testSalvar() {
        ProcessarEntidades form = new ProcessarEntidades();
        Salvar salvar = new Salvar(form, Salvar.SALVAR);
        assertEquals("", salvar.arquivo);
        assertEquals("", salvar.xml);
        assertNotNull(salvar.entidade);
        assertEquals(0, salvar.entidade.size());
        assertEquals(null, form.getName());
        assertEquals("Novo Projeto", form.getNomeArquivoSalvo());
        
        salvar = new Salvar(form, Salvar.SALVAR_XML);
        assertEquals("", salvar.arquivo);
        assertEquals("", salvar.xml);
        assertNotNull(salvar.entidade);
        assertEquals(0, salvar.entidade.size());
        assertEquals(null, form.getName());
        assertEquals("Novo Projeto", form.getNomeArquivoSalvo());
        
        salvar = new Salvar(form, Salvar.SALVAR_COMO);
        assertEquals("", salvar.arquivo);
        assertEquals("", salvar.xml);
        assertNotNull(salvar.entidade);
        assertEquals(0, salvar.entidade.size());
        assertEquals(null, form.getName());
        assertEquals("Novo Projeto", form.getNomeArquivoSalvo());
    }

    @Test
    public void testFiltro2() {
        Salvar salvar = new Salvar();
        Salvar.Filtro2 filtro = salvar.new Filtro2();
        assertTrue(filtro.accept(new File("file.jaw.xml")));
        assertTrue(filtro.accept(new File("file.JAW.XML")));
        assertFalse(filtro.accept(new File("directory")));
        assertFalse(filtro.accept(new File("file.xml")));
        assertEquals("XML JAW files", filtro.getDescription());
    }

    @Test
    public void testFiltro() {
        Salvar salvar = new Salvar();
        Salvar.Filtro filtro = salvar.new Filtro();
        assertTrue(filtro.accept(new File("file.jaw")));
        assertTrue(filtro.accept(new File("file.JAW")));
        assertFalse(filtro.accept(new File("directory")));
        assertFalse(filtro.accept(new File("file.xml")));
        assertEquals("JAW files", filtro.getDescription());
    }
}