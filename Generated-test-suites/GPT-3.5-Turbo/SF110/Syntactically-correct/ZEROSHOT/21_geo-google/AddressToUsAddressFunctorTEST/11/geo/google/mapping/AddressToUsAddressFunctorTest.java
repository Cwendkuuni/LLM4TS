import static org.junit.Assert.*;
import org.junit.Test;
import geo.google.datamodel.*;
import oasis.names.tc.ciq.xsdschema.xal._2.AddressDetails;
import java.util.List;

public class AddressToUsAddressFunctorTest {

    @Test
    public void testExecute() {
        AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();
        GeoAddress addr = new GeoAddress();
        AddressDetails addressDetails = new AddressDetails();
        GeoUsAddress result = functor.execute(addr);

        // Test setCountry method
        addr.getAddressDetails().getCountry().getCountryNameCode().add(new AddressDetails.Country.CountryNameCode());
        addr.getAddressDetails().getCountry().getCountryNameCode().get(0).setContent("USA");
        result = functor.execute(addr);
        assertEquals("USA", result.getCountry());

        // Test setState method
        AddressDetails.Country.AdministrativeArea administrativeArea = new AddressDetails.Country.AdministrativeArea();
        administrativeArea.getAdministrativeAreaName().add(new AddressDetails.Country.AdministrativeArea.AdministrativeAreaName());
        administrativeArea.getAdministrativeAreaName().get(0).setContent("California");
        addr.getAddressDetails().getCountry().setAdministrativeArea(administrativeArea);
        result = functor.execute(addr);
        assertEquals("California", result.getState());

        // Test setCounty method
        AddressDetails.Country.AdministrativeArea.SubAdministrativeArea subAdministrativeArea = new AddressDetails.Country.AdministrativeArea.SubAdministrativeArea();
        subAdministrativeArea.getSubAdministrativeAreaName().add(new AddressDetails.Country.AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName());
        subAdministrativeArea.getSubAdministrativeAreaName().get(0).setContent("Los Angeles");
        administrativeArea.setSubAdministrativeArea(subAdministrativeArea);
        result = functor.execute(addr);
        assertEquals("Los Angeles", result.getCounty());

        // Test setCity method
        GeoAddress.Locality locality = new GeoAddress.Locality();
        locality.getLocalityName().add(new GeoAddress.Locality.LocalityName());
        locality.getLocalityName().get(0).setContent("Santa Monica");
        administrativeArea.setLocality(locality);
        result = functor.execute(addr);
        assertEquals("Santa Monica", result.getCity());

        // Test setPostalCode method
        GeoAddress.Locality.PostalCode postalCode = new GeoAddress.Locality.PostalCode();
        postalCode.getPostalCodeNumber().add(new GeoAddress.Locality.PostalCode.PostalCodeNumber());
        postalCode.getPostalCodeNumber().get(0).setContent("90401");
        locality.setPostalCode(postalCode);
        result = functor.execute(addr);
        assertEquals("90401", result.getPostalCode());

        // Test setAddressLine1 and setAddressLine2 methods
        GeoAddress.Locality.Thoroughfare thoroughfare = new GeoAddress.Locality.Thoroughfare();
        thoroughfare.getThoroughfareName().add(new ThoroughfareNameType());
        thoroughfare.getThoroughfareName().get(0).setContent("Main St");
        locality.setThoroughfare(thoroughfare);
        result = functor.execute(addr);
        assertEquals("Main St", result.getAddressLine1());

        thoroughfare.getThoroughfareName().add(new ThoroughfareNameType());
        thoroughfare.getThoroughfareName().get(1).setContent("Apt 123");
        result = functor.execute(addr);
        assertEquals("Apt 123", result.getAddressLine2());

        // Test coordinate cloning
        GeoCoordinates coordinate = new GeoCoordinates();
        coordinate.setLatitude(34.0194);
        coordinate.setLongitude(-118.4912);
        addr.setCoordinate(coordinate);
        result = functor.execute(addr);
        assertNotSame(coordinate, result.getCoordinate());

        // Test accuracy
        addr.setAccuracy(0.75);
        result = functor.execute(addr);
        assertEquals(0.75, result.getAccuracy(), 0.001);
    }
}