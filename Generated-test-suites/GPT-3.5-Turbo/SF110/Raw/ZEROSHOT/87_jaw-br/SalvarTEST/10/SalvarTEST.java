import static org.junit.Assert.*;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class SalvarTest {

    @Test
    public void testSalvar() {
        ProcessarEntidades form = mock(ProcessarEntidades.class);
        int tipoSalvar = 20;

        Salvar salvar = new Salvar(form, tipoSalvar);

        assertTrue(salvar.xml.isEmpty());
        assertNotNull(salvar.entidade);
        assertNotNull(salvar.arquivo);
        assertFalse(salvar.vela);
    }

    @Test
    public void testSalvar_withException() {
        ProcessarEntidades form = mock(ProcessarEntidades.class);
        int tipoSalvar = 20;

        Salvar salvar = new Salvar(form, tipoSalvar);

        assertTrue(salvar.xml.isEmpty());
        assertNotNull(salvar.entidade);
        assertNotNull(salvar.arquivo);
        assertFalse(salvar.vela);

        when(form.getEntidades()).thenThrow(new Exception("Error"));

        salvar = new Salvar(form, tipoSalvar);

        assertTrue(salvar.xml.isEmpty());
        assertNotNull(salvar.entidade);
        assertNotNull(salvar.arquivo);
        assertFalse(salvar.vela);
    }

    @Test
    public void testSalvar_xml() {
        ProcessarEntidades form = mock(ProcessarEntidades.class);
        int tipoSalvar = 10;

        Salvar salvar = new Salvar(form, tipoSalvar);

        assertNotNull(salvar.xml);
        assertNotNull(salvar.entidade);
        assertNotNull(salvar.arquivo);
        assertFalse(salvar.vela);
    }

    @Test
    public void testFiltro2() {
        Salvar.Filtro2 filtro2 = new Salvar.Filtro2();

        assertTrue(filtro2.accept(new File("test.jaw.xml")));
        assertFalse(filtro2.accept(new File("test.txt")));

        assertEquals("XML JAW files", filtro2.getDescription());
    }

    @Test
    public void testFiltro() {
        Salvar.Filtro filtro = new Salvar.Filtro();

        assertTrue(filtro.accept(new File("test.jaw")));
        assertFalse(filtro.accept(new File("test.txt")));

        assertEquals("JAW files", filtro.getDescription());
    }

}