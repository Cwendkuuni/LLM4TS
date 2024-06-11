import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.ListPopulation;
import org.junit.Test;
import static org.junit.Assert.*;

public class ListPopulationTest {

    @Test
    public void testListPopulation() {
        ListPopulation population = new ListPopulation(5);
        assertEquals(5, population.getPopulationLimit());
        assertEquals(0, population.getPopulationSize());

        Chromosome chromosome1 = new Chromosome() {
            @Override
            public double getFitness() {
                return 1.0;
            }
        };
        Chromosome chromosome2 = new Chromosome() {
            @Override
            public double getFitness() {
                return 2.0;
            }
        };
        population.addChromosome(chromosome1);
        population.addChromosome(chromosome2);
        assertEquals(2, population.getPopulationSize());
        assertEquals(chromosome1, population.getChromosomes().get(0));
        assertEquals(chromosome2, population.getChromosomes().get(1));

        Chromosome fittest = population.getFittestChromosome();
        assertEquals(chromosome2, fittest);

        ListPopulation population2 = new ListPopulation(10);
        population2.addChromosomes(population.getChromosomes());
        assertEquals(7, population2.getPopulationSize());
        assertEquals(chromosome2, population2.getFittestChromosome());

        try {
            population2.setPopulationLimit(3);
            fail();
        } catch (NumberIsTooSmallException e) {
            // expected
        }
    }

    @Test(expected = NotPositiveException.class)
    public void testListPopulationNegativePopulationLimit() {
        ListPopulation population = new ListPopulation(-1);
    }

    @Test(expected = NullArgumentException.class)
    public void testListPopulationNullChromosomes() {
        ListPopulation population = new ListPopulation(5, null);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationBigChromosomesList() {
        List<Chromosome> chromosomes = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            chromosomes.add(new Chromosome() {
                @Override
                public double getFitness() {
                    return 1.0;
                }
            });
        }
        ListPopulation population = new ListPopulation(10, chromosomes);
    }
}