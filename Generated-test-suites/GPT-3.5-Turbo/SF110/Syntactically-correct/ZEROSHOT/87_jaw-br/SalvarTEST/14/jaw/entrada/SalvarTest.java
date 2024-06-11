import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class SalvarTest {

    Salvar salvar;

    @Before
    public void setUp() {
        ProcessarEntidades form = new ProcessarEntidades();
        int tipoSalvar = 10;
        salvar = new Salvar(form, tipoSalvar);
    }

    @Test
    public void testSalva() {
        assertNotNull(salvar);
        assertEquals(false, salvar.vela);
        assertEquals("", salvar.arquivo);
        assertEquals("", salvar.xml);
        assertNotNull(salvar.entidade);
        assertEquals(0, salvar.entidade.size());
    }

    @Test
    public void testESalvarXML() {
        ProcessarEntidades form = new ProcessarEntidades();
        int tipoSalvar = 10;
        Salvar salvarXML = new Salvar(form, tipoSalvar);
        assertNotNull(salvarXML);
        assertEquals(true, salvarXML.vela);
        assertNotNull(salvarXML.entidade);
        assertNotEquals(0, salvarXML.entidade.size());
        assertNotNull(salvarXML.arquivo);
        assertEquals(true, salvarXML.arquivo.endsWith(".jaw.xml"));
    }

    @Test
    public void testESalvar() {
        ProcessarEntidades form = new ProcessarEntidades();
        int tipoSalvar = 20;
        Salvar salvar = new Salvar(form, tipoSalvar);
        assertNotNull(salvar);
        assertEquals(true, salvar.vela);
        assertNotNull(salvar.entidade);
        assertNotEquals(0, salvar.entidade.size());
        assertNotNull(salvar.arquivo);
        assertEquals(true, salvar.arquivo.endsWith(".jaw"));
    }

    @Test
    public void testESalvarComo() {
        ProcessarEntidades form = new ProcessarEntidades();
        int tipoSalvar = 30;
        Salvar salvar = new Salvar(form, tipoSalvar);
        assertNotNull(salvar);
        assertEquals(true, salvar.vela);
        assertNotNull(salvar.entidade);
        assertNotEquals(0, salvar.entidade.size());
        assertNotNull(salvar.arquivo);
        assertEquals(true, salvar.arquivo.endsWith(".jaw"));
    }

    @Test
    public void testGetDescricaoFiltro2() {
        Salvar.Filtro2 filtro2 = salvar.new Filtro2();
        assertEquals("XML JAW files", filtro2.getDescription());
    }

    @Test
    public void testAcceptFiltro2() {
        Salvar.Filtro2 filtro2 = salvar.new Filtro2();
        File arquivo = new File("arquivo.jaw.xml");
        assertTrue(filtro2.accept(arquivo));
    }

    @Test
    public void testGetDescricaoFiltro() {
        Salvar.Filtro filtro = salvar.new Filtro();
        assertEquals("JAW files", filtro.getDescription());
    }

    @Test
    public void testAcceptFiltro() {
        Salvar.Filtro filtro = salvar.new Filtro();
        File arquivo = new File("arquivo.jaw");
        assertTrue(filtro.accept(arquivo));
    }
}