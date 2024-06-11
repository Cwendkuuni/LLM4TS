import static org.junit.Assert.assertEquals; 
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.fail;

import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.genetics.Chromosome;
import org.junit.Before; 
import org.junit.Test; 

import java.util.ArrayList; 
import java.util.List;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4; 

@RunWith(JUnit4.class)
public class ListPopulationTest {

    private ListPopulation listPopulation;

    @Before
    public void setUp() {
        listPopulation = new ListPopulation(10);
    }

    @Test
    public void testConstructor_PositivePopulationLimit() {
        try {
            List<Chromosome> chromosomes = new ArrayList<>();
            chromosomes.add(new Chromosome());
            chromosomes.add(new Chromosome());
            chromosomes.add(new Chromosome());
            listPopulation = new ListPopulation(chromosomes, 10);
            assertEquals(chromosomes, listPopulation.getChromosomes());
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testConstructor_NegativePopulationLimit() {
        assertThrows(NotPositiveException.class, () -> {
            listPopulation = new ListPopulation(-10);
        });
    }

    @Test
    public void testConstructor_NullChromosomes() {
        assertThrows(NullArgumentException.class, () -> {
            listPopulation = new ListPopulation(null, 10);
        });
    }

    @Test
    public void testConstructor_ChromosomesExceedPopulationLimit() {
        assertThrows(NumberIsTooLargeException.class, () -> {
            List<Chromosome> chromosomes = new ArrayList<>();
            chromosomes.add(new Chromosome());
            chromosomes.add(new Chromosome());
            chromosomes.add(new Chromosome());
            listPopulation = new ListPopulation(chromosomes, 2);
        });
    }

    @Test
    public void testSetChromosomes() {
        try {
            List<Chromosome> chromosomes = new ArrayList<>();
            chromosomes.add(new Chromosome());
            chromosomes.add(new Chromosome());
            chromosomes.add(new Chromosome());
            listPopulation = new ListPopulation(5);
            listPopulation.setChromosomes(chromosomes);
            assertEquals(chromosomes, listPopulation.getChromosomes());
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testSetChromosomes_NullChromosomes() {
        assertThrows(NullArgumentException.class, () -> {
            listPopulation.setChromosomes(null);
        });
    }

    @Test
    public void testSetChromosomes_ChromosomesExceedPopulationLimit() {
        assertThrows(NumberIsTooLargeException.class, () -> {
            List<Chromosome> chromosomes = new ArrayList<>();
            chromosomes.add(new Chromosome());
            chromosomes.add(new Chromosome());
            chromosomes.add(new Chromosome());
            listPopulation.setChromosomes(chromosomes);
        });
    }

    @Test
    public void testAddChromosomes() {
        try {
            List<Chromosome> chromosomes = new ArrayList<>();
            chromosomes.add(new Chromosome());
            chromosomes.add(new Chromosome());
            chromosomes.add(new Chromosome());
            listPopulation.addChromosomes(chromosomes);
            assertEquals(chromosomes.size(), listPopulation.getPopulationSize());
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testAddChromosomes_ExceedPopulationLimit() {
        assertThrows(NumberIsTooLargeException.class, () -> {
            List<Chromosome> chromosomes = new ArrayList<>();
            chromosomes.add(new Chromosome());
            chromosomes.add(new Chromosome());
            chromosomes.add(new Chromosome());
            chromosomes.add(new Chromosome());
            chromosomes.add(new Chromosome());
            listPopulation.addChromosomes(chromosomes);
        });
    }

    @Test
    public void testGetChromosomes() {
        assertEquals(0, listPopulation.getChromosomes().size());
    }

    @Test
    public void testGetChromosomeList() {
        List<Chromosome> chromosomes = listPopulation.getChromosomeList();
        assertEquals(chromosomes.size(), listPopulation.getPopulationSize());
    }

    @Test
    public void testAddChromosome() {
        try {
            listPopulation.addChromosome(new Chromosome());
            assertEquals(1, listPopulation.getPopulationSize());
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testAddChromosome_ExceedPopulationLimit() {
        assertThrows(NumberIsTooLargeException.class, () -> {
            for (int i = 0; i < 11; i++) {
                listPopulation.addChromosome(new Chromosome());
            }
        });
    }

    @Test
    public void testGetFittestChromosome() {
        Chromosome chromosome1 = new Chromosome();
        chromosome1.setFitness(1.0);
        Chromosome chromosome2 = new Chromosome();
        chromosome2.setFitness(0.5);
        listPopulation.addChromosome(chromosome1);
        listPopulation.addChromosome(chromosome2);
        assertEquals(chromosome1, listPopulation.getFittestChromosome());
    }

    @Test
    public void testGetPopulationLimit() {
        assertEquals(10, listPopulation.getPopulationLimit());
    }

    @Test
    public void testSetPopulationLimit_PositivePopulationLimit() {
        try {
            listPopulation.setPopulationLimit(20);
            assertEquals(20, listPopulation.getPopulationLimit());
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testSetPopulationLimit_NegativePopulationLimit() {
        assertThrows(NotPositiveException.class, () -> {
            listPopulation.setPopulationLimit(-20);
        });
    }

    @Test
    public void testSetPopulationLimit_SmallerThanCurrentPopulationSize() {
        assertThrows(NumberIsTooSmallException.class, () -> {
            listPopulation.setPopulationLimit(1);
        });
    }

    @Test
    public void testGetPopulationSize() {
        assertEquals(0, listPopulation.getPopulationSize());
    }

    @Test
    public void testToString() {
        assertEquals("", listPopulation.toString());
    }

    @Test
    public void testIterator() {
        assertEquals(false, listPopulation.iterator().hasNext());
    }
}