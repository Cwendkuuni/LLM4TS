import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.List;
import jaw.metamodelo.Entidade;

public class SalvarTest {
    
    private ProcessarEntidades form;
    private Salvar salvarXml;
    private Salvar salvar;
    private Salvar salvarComo;
    
    @Before
    public void setUp() {
        form = new ProcessarEntidades();
        salvarXml = new Salvar(form, 10);
        salvar = new Salvar(form, 20);
        salvarComo = new Salvar(form, 30);
    }
    
    @Test
    public void testSalvarXml() {
        assertEquals("Deve salvar em formato XML", ".jaw.xml", salvarXml.arquivo.substring(salvarXml.arquivo.length() - 8));
        assertNotNull("Deve conter entidades", salvarXml.xml);
    }

    @Test
    public void testSalvar() {
        assertNotNull("Deve conter entidades para salvar", salvar.entidade);
        assertNotNull("Deve conter um arquivo salvo", salvar.arquivo);
    }
    
    @Test
    public void testSalvarComo() {
        assertNotNull("Deve conter entidades para salvar como", salvarComo.entidade);
        assertNotNull("Deve conter um arquivo salvo como", salvarComo.arquivo);
    }
    
    @Test
    public void testFiltro2() {
        Salvar.Filtro2 filtro2 = new Salvar().new Filtro2();
        assertTrue("Deve aceitar arquivos .jaw.xml", filtro2.accept(new File("teste.jaw.xml")));
        assertEquals("Descrição do filtro deve ser XML JAW files", "XML JAW files", filtro2.getDescription());
    }
    
    @Test
    public void testFiltro() {
        Salvar.Filtro filtro = new Salvar().new Filtro();
        assertTrue("Deve aceitar arquivos .jaw", filtro.accept(new File("teste.jaw")));
        assertEquals("Descrição do filtro deve ser JAW files", "JAW files", filtro.getDescription());
    }
}