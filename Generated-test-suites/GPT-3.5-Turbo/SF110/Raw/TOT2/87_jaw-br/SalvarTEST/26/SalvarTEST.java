import static org.junit.Assert.*;
import org.junit.Test;

public class SalvarTest {

    @Test
    public void testSalvarConstructor() {
        ProcessarEntidades form = new ProcessarEntidades();
        Salvar salvar = new Salvar(form, 10);
        assertNotNull(salvar);
    }

    @Test
    public void testSalvarFileFilterAccept() {
        Salvar salva = new Salvar(new ProcessarEntidades(), 10);
        Salvar.Filtro filtro = salva.new Filtro();
        assertTrue(filtro.accept(new File("test.jaw")));
    }

    @Test
    public void testSalvarFileFilterDescription() {
        Salvar.Filtro filtro = new Salvar().new Filtro();
        assertEquals("JAW files", filtro.getDescription());
    }

    @Test
    public void testSalvarXMLFileFilterAccept() {
        Salvar salva = new Salvar(new ProcessarEntidades(), 10);
        Salvar.Filtro2 filtro = salva.new Filtro2();
        assertTrue(filtro.accept(new File("test.jaw.xml")));
    }

    @Test
    public void testSalvarXMLFileFilterDescription() {
        Salvar.Filtro2 filtro = new Salvar().new Filtro2();
        assertEquals("XML JAW files", filtro.getDescription());
    }
}