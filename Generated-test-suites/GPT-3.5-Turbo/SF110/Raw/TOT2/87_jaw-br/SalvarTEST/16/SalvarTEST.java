import static org.junit.Assert.*;
import org.junit.Test;
import jaw.entrada.Salvar;
import jaw.metamodelo.Entidade;
import jaw.gui.*;

public class SalvarTest {

    @Test
    public void testSalvarConstructor() {
        ProcessarEntidades form = new ProcessarEntidades();
        Salvar salvar = new Salvar(form, 10);
        assertNotNull(salvar);
    }

    @Test
    public void testSalvarConstructorEdgeCase() {
        ProcessarEntidades form = null;
        Salvar salvar = new Salvar(form, 10);
        assertNull(salvar);
    }

    @Test
    public void testSalvarWithInvalidFileType() {
        ProcessarEntidades form = new ProcessarEntidades();
        Salvar salvar = new Salvar(form, 20);
        assertNull(salvar.getArquivo());
    }

    @Test
    public void testSalvarWithInvalidEntities() {
        ProcessarEntidades form = new ProcessarEntidades();
        salvar.setEntidade(null);
        assertNull(salvar.getEntidades());
    }

    @Test
    public void testSalvarXMLExport() {
        ProcessarEntidades form = new ProcessarEntidades();
        Salvar salvar = new Salvar(form, 10);
        assertNotNull(salvar.getXml());
    }

    @Test
    public void testSalvarWithEmptyFileName() {
        ProcessarEntidades form = new ProcessarEntidades();
        form.setNomeArquivoSalvo("");
        Salvar salvar = new Salvar(form, 20);
        assertEquals(".jaw", salvar.getArquivo().substring(salvar.getArquivo().length() - 4));
    }

    @Test
    public void testFiltro2Accept() {
        Salvar.Filtro2 filtro = new Salvar().new Filtro2();
        assertTrue(filtro.accept(new File("test.jaw.xml")));
    }

    @Test
    public void testFiltroDescription() {
        Salvar.Filtro filtro = new Salvar().new Filtro();
        assertEquals("JAW files", filtro.getDescription());
    }
}