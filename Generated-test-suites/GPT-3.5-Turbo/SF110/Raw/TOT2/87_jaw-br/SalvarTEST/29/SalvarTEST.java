import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;
import java.io.IOException;

public class SalvarTest {

    @Test
    public void testSalvarXml() {
        // Typical Use Case
        ProcessarEntidades form = new ProcessarEntidades();
        int tipoSalvar = 10;
        Salvar salvar = new Salvar(form, tipoSalvar);
        
        // Assert
        assertNotNull(salvar.entidade);
        assertNotNull(salvar.xml);
    }

    @Test
    public void testSalvar() {
        // Typical Use Case
        ProcessarEntidades form = new ProcessarEntidades();
        int tipoSalvar = 20;
        Salvar salvar = new Salvar(form, tipoSalvar);
        
        // Assert
        assertNotNull(salvar.arquivo);
    }

    @Test
    public void testSalvarComo() {
        // Edge Case
        ProcessarEntidades form = new ProcessarEntidades();
        int tipoSalvar = 30;
        Salvar salvar = new Salvar(form, tipoSalvar);
        
        // Assert
        assertEquals(".jaw", salvar.arquivo.substring(salvar.arquivo.length() - 4));
    }

    @Test
    public void testFiltro2Accept() {
        Salvar.Filtro2 filtro = new Salvar().new Filtro2();
        File file = new File("test.jaw.xml");
        
        // Assert
        assertTrue(filtro.accept(file));
    }

    @Test
    public void testFiltro2Description() {
        Salvar.Filtro2 filtro = new Salvar().new Filtro2();
        
        // Assert
        assertEquals("XML JAW files", filtro.getDescription());
    }

    @Test
    public void testFiltroAccept() {
        Salvar.Filtro filtro = new Salvar().new Filtro();
        File file = new File("test.jaw");
        
        // Assert
        assertTrue(filtro.accept(file));
    }

    @Test
    public void testFiltroDescription() {
        Salvar.Filtro filtro = new Salvar().new Filtro();
        
        // Assert
        assertEquals("JAW files", filtro.getDescription());
    }

}