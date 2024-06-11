import static org.junit.Assert.*;
import org.junit.Test;
import java.io.*;
import javax.swing.*;

public class SalvarTest {

    @Test
    public void testSalvar() {
        ProcessarEntidades form = new ProcessarEntidades();
        int tipoSalvar = 20;
        Salvar salvar = new Salvar(form, tipoSalvar);

        assertTrue(salvar.vela);
        assertFalse(salvar.arquivo.isEmpty());
        assertFalse(salvar.xml.isEmpty());
        assertNotNull(salvar.entidade);

        for (Entidade entidade : salvar.entidade) {
            assertNotNull(entidade);
        }

        assertEquals(20, Salvar.SALVAR);
        assertEquals(30, Salvar.SALVAR_COMO);
    }

    @Test
    public void testSalvarXML() {
        ProcessarEntidades form = new ProcessarEntidades();
        int tipoSalvar = 10;
        Salvar salvar = new Salvar(form, tipoSalvar);

        assertFalse(salvar.vela);
        assertFalse(salvar.arquivo.isEmpty());
        assertFalse(salvar.xml.isEmpty());
        assertNotNull(salvar.entidade);

        for (Entidade entidade : salvar.entidade) {
            assertNotNull(entidade);
        }

        assertEquals(10, Salvar.SALVAR_XML);
    }

    @Test
    public void testFiltro2() {
        Salvar.Filtro2 filtro2 = new Salvar().new Filtro2();

        File validFile = new File("test.jaw.xml");
        assertTrue(filtro2.accept(validFile));

        File invalidFile = new File("test.txt");
        assertFalse(filtro2.accept(invalidFile));

        assertEquals("XML JAW files", filtro2.getDescription());
    }

    @Test
    public void testFiltro() {
        Salvar.Filtro filtro = new Salvar().new Filtro();

        File validFile = new File("test.jaw");
        assertTrue(filtro.accept(validFile));

        File invalidFile = new File("test.txt");
        assertFalse(filtro.accept(invalidFile));

        assertEquals("JAW files", filtro.getDescription());
    }
}

